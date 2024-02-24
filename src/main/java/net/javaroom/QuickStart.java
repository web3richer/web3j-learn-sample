package net.javaroom;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

public class QuickStart {

    public static void main(String[] args) {
        Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));

		try {
			Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
			String clientVersion = web3ClientVersion.getWeb3ClientVersion();
			System.out.println("clientVersion： " + clientVersion);
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}