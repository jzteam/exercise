package cn.jzteam.chain.web3j;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public class Web3jUtil {

    public static void main(String[] args) {
        try {
            Web3j web3j = Web3j.build(new HttpService(EnumChain.OKC.getUrl()));
            //
            String privateKey = "";
            Credentials credentials = Credentials.create(privateKey);
            System.out.println("credential.address=" + credentials.getAddress());

            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    // nonce
                    new BigInteger("1000002"),
                    // gasPrice
                    new BigInteger("1"),
                    // gasLimit
                    new BigInteger("20000"),
                    // to
                    "0x4662d17c309b88B4a70e8289Aa55106217faC464",
                    // data
                    "0xe0c86289"
            );
            byte[] raw = TransactionEncoder.signMessage(rawTransaction, (byte) 66, credentials);
            Request<?, EthSendTransaction> request = web3j.ethSendRawTransaction(Numeric.toHexString(raw));
            CompletableFuture<EthSendTransaction> ethSendTransactionCompletableFuture = request.sendAsync();
            EthSendTransaction ethSendTransaction = ethSendTransactionCompletableFuture.get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("transactionHash=" + transactionHash);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
