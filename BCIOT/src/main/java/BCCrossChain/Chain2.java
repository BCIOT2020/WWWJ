package BCCrossChain;

import Block.ECDSA;
import Block.Point;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.springframework.context.ApplicationContext;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

public class Chain2 {
    public static void main(String[] args) throws Exception {
        String msg = null;
        String signature = null;
        String ip = null;
        String PublicKey = null;
        GetService getService = new GetService();
        //获取链2服务
        ApplicationContext context_chain2 = getService.getContext("applicationContext1.xml");
        //获取链2上的web3J对象
        Web3j web3j1 = getService.getWeb3j(context_chain2);
        ServerSocket server = new ServerSocket(8887);
        Socket socket = server.accept();
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        ECDSA ecdsa = (ECDSA) objectInputStream.readObject();
        ECDSA app = ecdsa;
        byte[] bytes = new byte[1024];
        int len = is.read(bytes);
        String data = new String(bytes, 0, len);
        if (!(data.equals(null) && data.equals(""))) {
            msg = getService.getSplit(data, 0);
            System.out.println("message_digest" + msg);
            signature = getService.getSplit(data, 1);
            System.out.println("signature" + signature);
            PublicKey = getService.getSplit(data, 2);
            //进行验证
            boolean check = app.checkSignature(msg, signature);
            System.out.println("Signature verification: " + check);
            long end = System.currentTimeMillis();
            System.out.println("end" + end);
        }
        socket.close();
        server.close();

    }
}
