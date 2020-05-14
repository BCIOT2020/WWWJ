package BCCrossChain;


import Block.ECDSA;
import Block.Point;
import crosschain.GetService;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoderFactory;
import org.springframework.context.ApplicationContext;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

public class CrossChainMain {
    public static void main(String[] args) throws Exception {
        //准备部署和调用合约的参数
        BigInteger gasPrice = new BigInteger("300000000");
        BigInteger gasLimit = new BigInteger("300000000");
        //获取getService工具类对象
        GetService getService = new GetService();
        ////获取链1服务
        ApplicationContext context_chain1 = getService.getContext("applicationContext.xml");
        //获取web3J对象
        Web3j web3j = getService.getWeb3j(context_chain1);
        //user1用户,用于发出请求 传递信息
        Credentials credentials_user1 = getService.getCredentialsUsePEM(context_chain1);
        //获取请求 这里先模拟手动输入
        String request = getService.getRequest();

        //获取mid候选人
        Credentials credentials_mid = getService.getCredentialsUseP12(context_chain1);
        //获取mid地址
        String mid = getService.getAddress(credentials_mid);
        //链1上跨链合约地址
        String contractM_address = "0x888440b30047f77d1e11c9b7f6342a84f9e62a53";
        //部署Crosschain合约
        Crosschain contract_M = Crosschain.load(contractM_address, web3j, credentials_mid, new StaticGasProvider(gasPrice, gasLimit));
        //请求进入队列
        TransactionReceipt add = contract_M.addRequest(request).send();
        //请求出队
        TransactionReceipt pop = contract_M.popRequest().send();
        //用来解析交易的参数
        String abi = "[{\"constant\":false,\"inputs\":[{\"name\":\"d\",\"type\":\"string\"}],\"name\":\"addRequest\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"ChainIP\",\"type\":\"uint256\"},{\"name\":\"srclocalChainUser\",\"type\":\"address\"},{\"name\":\"destCrossChainUser\",\"type\":\"address\"},{\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"ReceiveUsersCrossChainMessage\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"popRequest\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]";
        String bin = "608060405234801561001057600080fd5b50604051602080610a638339810180604052810190808051906020019092919050505033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060056003600001816100c791906100ce565b5050610193565b8154818355818111156100f5578183600052602060002091820191016100f491906100fa565b5b505050565b61012391905b8082111561011f57600081816101169190610126565b50600101610100565b5090565b90565b50805460018160011615610100020316600290046000825580601f1061014c575061016b565b601f01602090049060005260206000209081019061016a919061016e565b5b50565b61019091905b8082111561018c576000816000905550600101610174565b5090565b90565b6108c1806101a26000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063128d72151461005c578063582a3500146100c5578063e4690a0b1461013c575b600080fd5b34801561006857600080fd5b506100c3600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506101cc565b005b3480156100d157600080fd5b5061013a60048036038101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506101da565b005b34801561014857600080fd5b506101516105e3565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610191578082015181840152602081019050610176565b50505050905090810190601f1680156101be5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101d76003826105f4565b50565b600080600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1691508173ffffffffffffffffffffffffffffffffffffffff1663dd62ed3e86306040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200192505050602060405180830381600087803b1580156102d157600080fd5b505af11580156102e5573d6000803e3d6000fd5b505050506040513d60208110156102fb57600080fd5b81019080805190602001909291905050509050600081111515610386576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f7573657220646f206e6f74206465706f73697420616e792076616c756500000081525060200191505060405180910390fd5b8360008088815260200190815260200160002060008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600080600088815260200190815260200160002060008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515156104a657fe5b8173ffffffffffffffffffffffffffffffffffffffff166323b872dd86600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16846040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561059f57600080fd5b505af11580156105b3573d6000803e3d6000fd5b505050506040513d60208110156105c957600080fd5b810190808051906020019092919050505050505050505050565b60606105ef600361067e565b905090565b81600101548260000180549050600184600201540181151561061257fe5b061415610624576106228261067e565b505b8082600001836002015481548110151561063a57fe5b9060005260206000200190805190602001906106579291906107a8565b508160000180549050600183600201540181151561067157fe5b0682600201819055505050565b60606000826001015483600201541415610697576107a2565b8260000183600101548154811015156106ac57fe5b906000526020600020019050808054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561074d5780601f106107225761010080835404028352916020019161074d565b820191906000526020600020905b81548152906001019060200180831161073057829003601f168201915b5050505050915082600001836001015481548110151561076957fe5b90600052602060002001600061077f9190610828565b8260000180549050600184600101540181151561079857fe5b0683600101819055505b50919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106107e957805160ff1916838001178555610817565b82800160010185558215610817579182015b828111156108165782518255916020019190600101906107fb565b5b5090506108249190610870565b5090565b50805460018160011615610100020316600290046000825580601f1061084e575061086d565b601f01602090049060005260206000209081019061086c9190610870565b5b50565b61089291905b8082111561088e576000816000905550600101610876565b5090565b905600a165627a7a723058207a309d77213cfc78e482cfc47464da79a060aabdadca762b4c3145da1bddbef00029";
        TransactionDecoder txDecodeSampleDecoder = TransactionDecoderFactory.buildTransactionDecoder(abi, bin);
        String input = pop.getInput();
        String output = pop.getOutput();
        //解析交易输出
        String jsonResult = txDecodeSampleDecoder.decodeOutputReturnJson(input, output);
        System.out.println("jsonResult" + jsonResult);
        String result = getService.parseJson(jsonResult, "result");
        String result_sub = getService.getSubString(result);
        //返回我们要的信息
        String data = getService.parseJson(result_sub, "data");
        System.out.println("data" + data);
        //链2 IP地址
        String ChainIP = getService.getSplit(data, 0);
        //链1
        String srcLocalChainUser = getService.getSplit(data, 1);
        //链2
        String destCrossChainUser = getService.getSplit(data, 2);
        //数字签名验证的信息
        String message = getService.getSplit(data, 3);
        long start = System.currentTimeMillis();
        System.out.println("start" + start);
        //用sha256算法对信息取摘要
        String message_digest = getService.getSHA256StrJava(message);
        System.out.println("message_digest: " + message_digest);

        ECDSA app = new ECDSA();
        BigInteger dA = BigInteger.valueOf(7);
        app.setdA(dA);
        Point QA = app.getQA();
        //获取私钥
        String PrivateKey = String.valueOf(dA);
        System.out.println("PrivateKey:" + PrivateKey);
        //获取公钥
        String PublicKey = QA.getX() + "," + QA.getY();
        System.out.println("PublicKey:" + PublicKey);
        System.out.println("Private key of sender 'A': " + dA);
        System.out.println("Public key of sender 'A': QA(" + QA.getX() + "," + QA.getY() + ")");
        //对摘要进行加密
        String signature = app.signingMessage(message_digest);
        System.out.println("message_digest: " + message_digest);
        System.out.println("Signature: " + signature);
        System.out.println("length: " + signature.length());

        //与链2通信 将摘要  密文 公钥 发送给链2
        Socket socket_chain2 = new Socket(ChainIP, 8887);
        OutputStream os1 = socket_chain2.getOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(socket_chain2.getOutputStream());
        //讲app对象发送过去
        out.writeObject(app);
        os1.write((message_digest + "," + signature + "," + PublicKey).getBytes());
    }
}

