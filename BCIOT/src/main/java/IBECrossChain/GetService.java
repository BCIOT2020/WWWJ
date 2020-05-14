package IBECrossChain;

import com.alibaba.fastjson.JSON;
import org.fisco.bcos.channel.client.P12Manager;
import org.fisco.bcos.channel.client.PEMManager;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Scanner;

public class GetService {

    //获取context对象
    public ApplicationContext getContext(String xml) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:" + xml);
        return context;
    }

    //获取web3j对象
    public Web3j getWeb3j(ApplicationContext context) throws Exception {
        Service service = context.getBean(Service.class);
        service.run();
        ChannelEthereumService channelEthereumService = new ChannelEthereumService();
        channelEthereumService.setChannelService(service);
        channelEthereumService.setTimeout(10000);
        Web3j web3j = Web3j.build(channelEthereumService, service.getGroupId());
        return web3j;
    }

    //通过PEMManager.class获取credential,用于发布交易
    public Credentials getCredentialsUsePEM(ApplicationContext context) throws UnrecoverableKeyException, InvalidKeySpecException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        PEMManager pem = context.getBean(PEMManager.class);
        ECKeyPair pemKeyPair = pem.getECKeyPair();
        String PrivateKey = pemKeyPair.getPrivateKey().toString(16);
     /*   String PublicKey=pemKeyPair.getPublicKey().toString(16);
        System.out.println("私钥："+PrivateKey);
        System.out.println("================");
        System.out.println("公钥："+PublicKey);*/
        //user1的账户私钥用于交易签名，msg.sender是user1
        Credentials credentialsPEM = GenCredential.create(PrivateKey);
        return credentialsPEM;
    }

    //通过P12Manager.class获取credential,用于发布交易
    public Credentials getCredentialsUseP12(ApplicationContext context) throws UnrecoverableKeyException, InvalidKeySpecException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        P12Manager pem = context.getBean(P12Manager.class);
        ECKeyPair pemKeyPair = pem.getECKeyPair();
        String PrivateKey = pemKeyPair.getPrivateKey().toString(16);
        //user1的账户私钥用于交易签名，msg.sender是user1
        Credentials credentialsP12 = GenCredential.create(PrivateKey);
        return credentialsP12;
    }

    //获取地址
    public String getAddress(Credentials credentials) {
        String address = credentials.getAddress();
        return address;
    }

    //解析字符串
    public String parseJson(String str, String s) {
        String temp = null;
        Map maps = (Map) JSON.parse(str);
        for (Object map : maps.entrySet()) {
            if (((Map.Entry) map).getKey().equals(s)) {
                temp = ((Map.Entry) map).getValue().toString();
            }
        }
        return temp;
    }

    //截取字符串
    public String getSubString(String o) {
        return o.substring(1, o.length() - 1);
    }

    //键盘输入请求
    public String getRequest() {
        return new Scanner(System.in).next();
    }

    //获取分割好的字符串
    public String getSplit(String str, int num) {
        String[] Strs = str.split(",");
        return Strs[num];
    }
    /**
     * 利用java原生的摘要实现SHA256加密
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }
    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


}