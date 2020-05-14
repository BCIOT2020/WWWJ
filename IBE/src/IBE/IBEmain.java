package IBE;

import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

public class IBEmain {
    public static void main(String[] args) throws IOException {
        String PrivateKey = null;
        String PublicKey = null;
        String decrypt_text = null;
        String encrypt_text = null;
        BBGHIBECiphertext cf = null;
        BBGHIBE bbghibe = new BBGHIBE();
        String[] testI1 = {"Depth 1"};
        String testI2 = "Depth 2";
        String testI3 = "Depth 3";
        String testI4 = "Depth 4";
        String testI5 = "Depth 5";
        String testI6 = "Depth 6";
        String testI7 = "Depth 7";
        String[] receiver = new String[7];
        receiver[0] = testI1[0];
        receiver[1] = testI2;
        receiver[2] = testI3;
        receiver[3] = testI4;
        receiver[4] = testI5;
        receiver[5] = testI6;
        receiver[6] = testI7;
        String[] ciphertextIV = new String[7];

        BBGHIBEMasterKey msk = bbghibe.Setup("a.properties", 7);
        BBGHIBESecretKey SKDepth1 = bbghibe.KeyGen(msk, testI1);
        BBGHIBESecretKey SKDepth2 = bbghibe.Delegate(SKDepth1, testI2);
        BBGHIBESecretKey SKDepth3 = bbghibe.Delegate(SKDepth2, testI3);
        BBGHIBESecretKey SKDepth4 = bbghibe.Delegate(SKDepth3, testI4);
        BBGHIBESecretKey SKDepth5 = bbghibe.Delegate(SKDepth4, testI5);
        BBGHIBESecretKey SKDepth6 = bbghibe.Delegate(SKDepth5, testI6);
        BBGHIBESecretKey SKDepth7 = bbghibe.Delegate(SKDepth6, testI7);
        ServerSocket server = new ServerSocket(8888);

        while (true) {
            Socket socket = server.accept();
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            byte[] bytes = new byte[1024];

            int len = is.read(bytes);
            String data = new String(bytes, 0, len);
            if (!(data.equals(null) && data.equals(""))) {
                if (!data.startsWith("+")) {
                    System.out.println("客户端传输的消息: " + data);
                    //ibe产生一对公私钥
                    PrivateKey = bbghibe.PrivateKey;
                    System.out.println("PrivateKey: " + PrivateKey);
                    PublicKey = bbghibe.PublicKey;
                    System.out.println("PublicKey: " + PublicKey);
                    //并用私钥对获取的信息加密
                    cf = bbghibe.Encrypt(receiver, data);
                    encrypt_text = bbghibe.encrypt_text;
                    System.out.println("encrypt_text: " + encrypt_text);

                    //把私钥 公钥 密文发送回给链1
                    String result = PrivateKey + "," + PublicKey + "," + encrypt_text;
                    System.out.println("result: " + result);
                    os.write(result.getBytes());


                } else {
                    data = data.substring(1, data.length());
                    System.out.println("data: " + data);
                    //对密文进行解密
                    Element decrypt = bbghibe.decrypt(receiver, cf, SKDepth1);
                    BigInteger bigInteger = decrypt.toBigInteger();
                    decrypt_text = bbghibe.numberToLetter(bigInteger);
                    System.out.println("decrypt_text" + decrypt_text);
                    /*Element decrypt = bbghibe.decrypt(receiver, cf, SKDepth1);
                    BigInteger bigInteger = decrypt.toBigInteger();
                    String decrypt_text = bbghibe.numberToLetter(bigInteger);*/
                    //将解密后的原文返回给链2
                    os.write(decrypt_text.getBytes());
                }
            }


        }
    }
}