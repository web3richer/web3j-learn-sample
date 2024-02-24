package net.javaroom;

import com.google.common.collect.ImmutableList;
import org.bitcoinj.crypto.*;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.exception.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;

/**
 * @author lizhongyuan
 */
public class CreateWallet1 {

    public static void main(String[] args) throws Exception {
        //创建钱包
        createWallet();
    }

    /**
     * 创建钱包
     */
    private static void createWallet() throws MnemonicException.MnemonicLengthException, CipherException, IOException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
        secureRandom.nextBytes(entropy);
        // 生成12位助记词
        List<String>  str = MnemonicCode.INSTANCE.toMnemonic(entropy);
        // 使用助记词生成钱包种子
        byte[] seed = MnemonicCode.toSeed(str, "");
        DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
        DeterministicKey deterministicKey = deterministicHierarchy
                .deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
        byte[] bytes = deterministicKey.getPrivKeyBytes();
        ECKeyPair keyPair = ECKeyPair.create(bytes);
        // 生成 Keystore
        String walletFile = walletFile(keyPair);
        //通过公钥生成钱包地址
        String address = Keys.getAddress(keyPair.getPublicKey());
        System.out.println();
        System.out.println("助记词：");
        System.out.println(str);
        System.out.println();
        System.out.println("地址：");
        System.out.println("0x"+address);
        System.out.println();
        System.out.println("私钥：");
        System.out.println("0x"+keyPair.getPrivateKey().toString(16));
        System.out.println();
        System.out.println("公钥：");
        System.out.println(keyPair.getPublicKey().toString(16));
        System.out.println();
        System.out.println("wallFile：");
        System.out.println(walletFile);
    }

    /**
     * path路径
     */
    private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
            ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
                    ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);

    private static String walletFile(ECKeyPair ecKeyPair) throws CipherException, IOException {
        String walletFilePath="E:/file";
        return WalletUtils.generateWalletFile("password", ecKeyPair, new File(walletFilePath), false);
    }

}