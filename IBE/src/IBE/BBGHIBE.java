package IBE;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BBGHIBE {
    public String PublicKey;
    public String PrivateKey;
    public String encrypt_text;
    public static final boolean isDebug = true;
    public Pairing pairing;
    private int MAX_DEPTH;
    //Public parameters
    public Element g;
    public Element h;
    public Element[] u;
    public Element E_g_g;

    /**
     * System setup algorithms, takes the max depth of hierarchy as input, and outputs the master secret key
     *
     * @param perperties The file name of the elliptic curve parameters
     * @param D          Maximal depth of hierarchy
     * @return Master Secret Key
     */
    public BBGHIBEMasterKey Setup(String perperties, int D) {
        // Generate curve parameters
        pairing = PairingFactory.getPairing(perperties);

        this.MAX_DEPTH = D;

        //generate alpha
        Element alpha = pairing.getZr().newRandomElement().getImmutable();
        PublicKey = alpha.toString();

        // Generate public parameters
        this.g = pairing.getG1().newRandomElement().getImmutable();
        this.h = pairing.getG1().newRandomElement().getImmutable();
        this.u = new Element[this.MAX_DEPTH];
        for (int i = 0; i < this.u.length; i++) {
            this.u[i] = pairing.getG1().newRandomElement().getImmutable();
        }
        this.E_g_g = pairing.pairing(this.g, this.g).powZn(alpha).getImmutable();

        //generate master secret key
        BBGHIBEMasterKey masterKey = new BBGHIBEMasterKey();
        masterKey.alpha = alpha.duplicate().getImmutable();
        return masterKey;
    }

    /**
     * Key Generation algorithm to generate secret key associated with the given identity vector
     *
     * @param msk            master secret key
     * @param identityVector the given identity vector
     * @return secret key associated with the given identity vector
     */
    public BBGHIBESecretKey KeyGen(BBGHIBEMasterKey msk, String[] identityVector) {
        //Determine the validity of Identity Vector
        assert (identityVector.length <= this.MAX_DEPTH);

        //generate the secret key
        Element r = pairing.getZr().newRandomElement().getImmutable();
        PrivateKey = r.toString();
        BBGHIBESecretKey secretKey = new BBGHIBESecretKey();
        secretKey.identityVector = new String[identityVector.length];
        System.arraycopy(identityVector, 0, secretKey.identityVector, 0, identityVector.length);

        //compute K_1
        secretKey.K_1 = this.g.powZn(r).getImmutable();

        //compute K_2
        secretKey.K_2 = this.h.duplicate();
        for (int i = 0; i < identityVector.length; i++) {
            secretKey.K_2 = secretKey.K_2.mul(this.u[i].powZn(Util.hash_id(pairing, identityVector[i])));
        }
        secretKey.K_2 = secretKey.K_2.powZn(r);
        secretKey.K_2 = secretKey.K_2.mul(this.g.powZn(msk.alpha)).getImmutable();

        //compute E
        secretKey.E = new Element[this.MAX_DEPTH];
        for (int i = identityVector.length; i < this.MAX_DEPTH; i++) {
            secretKey.E[i] = this.u[i].powZn(r).getImmutable();
        }
        return secretKey;
    }

    /**
     * Delegation algorithm to delegate a secret key for the user's subordinate
     *
     * @param secretkey the secret key for the supervisor
     * @param identity  the identity for the user's subordinate
     * @return secret key for the user's subordinate
     */
    public BBGHIBESecretKey Delegate(BBGHIBESecretKey secretkey, String identity) {
        //Determine the validity of Identity Vector
        assert (secretkey.identityVector.length < this.MAX_DEPTH);

        //delegate the secret key
        BBGHIBESecretKey delegateKey = new BBGHIBESecretKey();
        String[] delegateIV = new String[secretkey.identityVector.length + 1];
        System.arraycopy(secretkey.identityVector, 0, delegateIV, 0, secretkey.identityVector.length);
        delegateIV[secretkey.identityVector.length] = identity;
        delegateKey.identityVector = delegateIV;
        Element r = pairing.getZr().newRandomElement().getImmutable();

        //compute K_1
        delegateKey.K_1 = secretkey.K_1.duplicate();
        delegateKey.K_1 = delegateKey.K_1.mul(this.g.powZn(r)).getImmutable();

        //compute K_2
        delegateKey.K_2 = this.h.duplicate();
        for (int i = 0; i < delegateIV.length; i++) {
            delegateKey.K_2 = delegateKey.K_2.mul(this.u[i].powZn(Util.hash_id(pairing, delegateIV[i])));
        }
        delegateKey.K_2 = delegateKey.K_2.powZn(r);
        delegateKey.K_2 = delegateKey.K_2.mul(secretkey.K_2);
        delegateKey.K_2 = delegateKey.K_2.mul(secretkey.E[secretkey.identityVector.length].powZn(Util.hash_id(pairing, identity))).getImmutable();

        //compute b
        delegateKey.E = new Element[this.MAX_DEPTH];
        for (int i = secretkey.identityVector.length; i < this.MAX_DEPTH; i++) {
            delegateKey.E[i] = this.u[i].powZn(r).mul(secretkey.E[i]).getImmutable();
        }
        return delegateKey;
    }

    /**
     * Encrypt algorithm to an identity vector
     *
     * @param identityVector the target identity vector
     * @return the ciphertext
     */
    public BBGHIBECiphertext Encrypt(String[] identityVector, String line) {
        //Determine the validity of Identity Vector
        assert (identityVector.length <= this.MAX_DEPTH);

        //Generate a random message
        Element message = pairing.getGT().newRandomElement();
        encrypt_text = message.toString();

        //int message_int = this.letterToNumber(message_str);
        // message.set(new BigInteger(String.valueOf(message_int)));


        message.set(letterToNumber(line));

        if (isDebug) {
            //System.out.println("Infor - encrypt: the generated random message is " + message);
        }

        //Encrypt that message
        Element s = pairing.getZr().newRandomElement().getImmutable();
        BBGHIBECiphertext ciphertext = new BBGHIBECiphertext();
        //compute C_0
        ciphertext.C_0 = this.E_g_g.powZn(s).mul(message).getImmutable();

        //compute C_2`
        ciphertext.C_2 = this.g.powZn(s).getImmutable();

        //compute c_1
        ciphertext.C_1 = this.h.duplicate();
        for (int i = 0; i < identityVector.length; i++) {
            ciphertext.C_1 = ciphertext.C_1.mul(this.u[i].powZn(Util.hash_id(pairing, identityVector[i])));
        }
        ciphertext.C_1 = ciphertext.C_1.powZn(s).getImmutable();
        return ciphertext;
    }

    /**
     * Decrypt the ciphertext using a secret key
     *
     * @param identityVector the receive identity vector
     * @param ciphertext     the ciphertext
     * @param secretKey      the secret key for the receiver or for the receiver's supervisor
     * @return the message
     */
    public Element decrypt(String[] identityVector, BBGHIBECiphertext ciphertext, BBGHIBESecretKey secretKey) {
        //Determine the validity of Identity Vector, the ciphertext and the secret key
        //Secret Key Identity Vector depth needs to be smaller than ciphertext Identity Vector depth
        assert (identityVector.length >= secretKey.identityVector.length);

        //the identity vector for the secret key should match the receiver's identity vector
        for (int i = 0; i < secretKey.identityVector.length; i++) {
            assert (secretKey.identityVector[i].equals(identityVector[i]));
        }

        Element K = secretKey.K_2.duplicate();
        for (int i = secretKey.identityVector.length; i < identityVector.length; i++) {
            K.mul(secretKey.E[i].powZn(Util.hash_id(pairing, identityVector[i])));
        }
        Element message = pairing.pairing(secretKey.K_1, ciphertext.C_1);
        message = message.div(pairing.pairing(K, ciphertext.C_2));
        message = message.mul(ciphertext.C_0);
//        System.out.println("Infor - decrypt: the message is " + numberToLetter(message.toBigInteger()));
        /*
        String str_message[] = new String[2];
        str_message = message.toString().split(",");
        String str_message_x[] = new String[2];
        str_message_x = str_message[0].split("=");
        //System.out.println(str_message_x[1]);
        int message_int = this.excelColStrToNum("stop","stop".length());
        String message_str = this.excelColIndexToStr(message_int);
        System.out.println(message_str);
        */
        return message;
    }


    public String numberToLetter(BigInteger num) {
        String str_num = num.toString();
        StringBuilder str = new StringBuilder("");
        for (int i = 0; i < str_num.length(); ) {
            if (Integer.parseInt("" + str_num.charAt(i)) != 1) {
                str.append((char) Integer.parseInt(str_num.substring(i, i + 2)));
                i = i + 2;
            } else {
                str.append((char) Integer.parseInt(str_num.substring(i, i + 3)));
                i = i + 3;
            }
        }
        return str.toString();
    }

    public BigInteger letterToNumber(String letter) {
        List<String> strs = BBGHIBE.getStrList(letter, 1);
        StringBuilder temp = new StringBuilder("");
        for (String str : strs) {
            temp.append(String.valueOf((int) str.charAt(0)));
            //System.out.println(temp);
        }
        BigInteger res = new BigInteger(temp.toString());
        return res;
    }

    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    public static List<String> getStrList(String inputString, int length,
                                          int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }


    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        System.out.println(sbu.toString());
        return sbu.toString();
    }

    public Pairing getPairing() {
        return pairing;
    }
}

