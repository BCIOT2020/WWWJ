package IBE;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class Util {
    public static Element hash_id(Pairing pairing, String id){
        byte[] byte_identity = id.getBytes();
        Element hash = pairing.getZr().newElement().setFromHash(byte_identity, 0, byte_identity.length);
        return hash;
    }
}


