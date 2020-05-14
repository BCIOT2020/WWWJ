package BCCrossChain;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class Crosschain extends Contract {
    public static String BINARY = "608060405234801561001057600080fd5b50604051602080610a638339810180604052810190808051906020019092919050505033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060056003600001816100c791906100ce565b5050610193565b8154818355818111156100f5578183600052602060002091820191016100f491906100fa565b5b505050565b61012391905b8082111561011f57600081816101169190610126565b50600101610100565b5090565b90565b50805460018160011615610100020316600290046000825580601f1061014c575061016b565b601f01602090049060005260206000209081019061016a919061016e565b5b50565b61019091905b8082111561018c576000816000905550600101610174565b5090565b90565b6108c1806101a26000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063128d72151461005c578063582a3500146100c5578063e4690a0b1461013c575b600080fd5b34801561006857600080fd5b506100c3600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506101cc565b005b3480156100d157600080fd5b5061013a60048036038101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506101da565b005b34801561014857600080fd5b506101516105e3565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610191578082015181840152602081019050610176565b50505050905090810190601f1680156101be5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101d76003826105f4565b50565b600080600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1691508173ffffffffffffffffffffffffffffffffffffffff1663dd62ed3e86306040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200192505050602060405180830381600087803b1580156102d157600080fd5b505af11580156102e5573d6000803e3d6000fd5b505050506040513d60208110156102fb57600080fd5b81019080805190602001909291905050509050600081111515610386576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f7573657220646f206e6f74206465706f73697420616e792076616c756500000081525060200191505060405180910390fd5b8360008088815260200190815260200160002060008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600080600088815260200190815260200160002060008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515156104a657fe5b8173ffffffffffffffffffffffffffffffffffffffff166323b872dd86600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16846040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561059f57600080fd5b505af11580156105b3573d6000803e3d6000fd5b505050506040513d60208110156105c957600080fd5b810190808051906020019092919050505050505050505050565b60606105ef600361067e565b905090565b81600101548260000180549050600184600201540181151561061257fe5b061415610624576106228261067e565b505b8082600001836002015481548110151561063a57fe5b9060005260206000200190805190602001906106579291906107a8565b508160000180549050600183600201540181151561067157fe5b0682600201819055505050565b60606000826001015483600201541415610697576107a2565b8260000183600101548154811015156106ac57fe5b906000526020600020019050808054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561074d5780601f106107225761010080835404028352916020019161074d565b820191906000526020600020905b81548152906001019060200180831161073057829003601f168201915b5050505050915082600001836001015481548110151561076957fe5b90600052602060002001600061077f9190610828565b8260000180549050600184600101540181151561079857fe5b0683600101819055505b50919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106107e957805160ff1916838001178555610817565b82800160010185558215610817579182015b828111156108165782518255916020019190600101906107fb565b5b5090506108249190610870565b5090565b50805460018160011615610100020316600290046000825580601f1061084e575061086d565b601f01602090049060005260206000209081019061086c9190610870565b5b50565b61089291905b8082111561088e576000816000905550600101610876565b5090565b905600a165627a7a723058207a309d77213cfc78e482cfc47464da79a060aabdadca762b4c3145da1bddbef00029";

    public static final String ABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"d\",\"type\":\"string\"}],\"name\":\"addRequest\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"destChainId\",\"type\":\"uint256\"},{\"name\":\"srclocalChainUser\",\"type\":\"address\"},{\"name\":\"destCrossChainUser\",\"type\":\"address\"},{\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"ReceiveUsersCrossChainMessage\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"popRequest\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]";

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC_ADDREQUEST = "addRequest";

    public static final String FUNC_RECEIVEUSERSCROSSCHAINMESSAGE = "ReceiveUsersCrossChainMessage";

    public static final String FUNC_POPREQUEST = "popRequest";

    @Deprecated
    protected Crosschain(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Crosschain(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Crosschain(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Crosschain(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<TransactionReceipt> addRequest(String d) {
        final Function function = new Function(
                FUNC_ADDREQUEST, 
                Arrays.<Type>asList(new Utf8String(d)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void addRequest(String d, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ADDREQUEST, 
                Arrays.<Type>asList(new Utf8String(d)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String addRequestSeq(String d) {
        final Function function = new Function(
                FUNC_ADDREQUEST, 
                Arrays.<Type>asList(new Utf8String(d)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<String> getAddRequestInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDREQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> ReceiveUsersCrossChainMessage(BigInteger destChainId, String srclocalChainUser, String destCrossChainUser, BigInteger amount) {
        final Function function = new Function(
                FUNC_RECEIVEUSERSCROSSCHAINMESSAGE, 
                Arrays.<Type>asList(new Uint256(destChainId),
                new Address(srclocalChainUser),
                new Address(destCrossChainUser),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void ReceiveUsersCrossChainMessage(BigInteger destChainId, String srclocalChainUser, String destCrossChainUser, BigInteger amount, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_RECEIVEUSERSCROSSCHAINMESSAGE, 
                Arrays.<Type>asList(new Uint256(destChainId),
                new Address(srclocalChainUser),
                new Address(destCrossChainUser),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String ReceiveUsersCrossChainMessageSeq(BigInteger destChainId, String srclocalChainUser, String destCrossChainUser, BigInteger amount) {
        final Function function = new Function(
                FUNC_RECEIVEUSERSCROSSCHAINMESSAGE, 
                Arrays.<Type>asList(new Uint256(destChainId),
                new Address(srclocalChainUser),
                new Address(destCrossChainUser),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple4<BigInteger, String, String, BigInteger> getReceiveUsersCrossChainMessageInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_RECEIVEUSERSCROSSCHAINMESSAGE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple4<BigInteger, String, String, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> popRequest() {
        final Function function = new Function(
                FUNC_POPREQUEST, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void popRequest(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_POPREQUEST, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String popRequestSeq() {
        final Function function = new Function(
                FUNC_POPREQUEST, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<String> getPopRequestOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_POPREQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    @Deprecated
    public static Crosschain load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Crosschain(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Crosschain load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Crosschain(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Crosschain load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Crosschain(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Crosschain load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Crosschain(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Crosschain> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String dest) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(dest)));
        return deployRemoteCall(Crosschain.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Crosschain> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String dest) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(dest)));
        return deployRemoteCall(Crosschain.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Crosschain> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String dest) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(dest)));
        return deployRemoteCall(Crosschain.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Crosschain> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String dest) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(dest)));
        return deployRemoteCall(Crosschain.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
