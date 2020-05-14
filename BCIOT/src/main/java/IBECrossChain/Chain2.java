package IBECrossChain;

import org.fisco.bcos.web3j.protocol.Web3j;
import org.springframework.context.ApplicationContext;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Chain2 {
    public static void main(String[] args) throws Exception {
        String PublicKey = null;
        String encrypt_text = null;
        String ip = null;
        String message_digest = null;
        GetService getService = new GetService();
        //获取链2服务
        ApplicationContext context_chain2 = getService.getContext("applicationContext1.xml");
        //获取链2上的web3J对象
        Web3j web3j1 = getService.getWeb3j(context_chain2);

        ServerSocket server = new ServerSocket(8887);
        Socket socket = server.accept();
        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len = is.read(bytes);
        String data = new String(bytes, 0, len);
        if (!(data.equals(null) && data.equals(""))) {
            PublicKey = getService.getSplit(data, 0);
            System.out.println("PublicKey: " + PublicKey);
            encrypt_text = getService.getSplit(data, 1) + "," + getService.getSplit(data, 2);
            System.out.println("encrypt_text: " + encrypt_text);
            ip = getService.getSplit(data, 3);
            message_digest = getService.getSplit(data, 4);
        }
        socket.close();
        server.close();

        Socket socket_ibe = new Socket(ip, 8888);
        OutputStream os1 = socket_ibe.getOutputStream();
        InputStream is1 = socket_ibe.getInputStream();
        os1.write(("+" + encrypt_text + "," + PublicKey).getBytes());

        byte[] bytes1 = new byte[1024];
        int len1 = is1.read(bytes1);
        String decrypt_text = new String(bytes1, 0, len1);
        if (decrypt_text.equals(message_digest)) {
            System.out.println("签名成功");
            long end = System.currentTimeMillis();
            System.out.println(end);
        } else {
            System.out.println("签名失败，数据内容可能被更改！");
            long end = System.currentTimeMillis();
            System.out.println(end);
        }
    }
}
