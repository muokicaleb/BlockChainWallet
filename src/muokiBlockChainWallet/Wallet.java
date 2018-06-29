package muokiBlockChainWallet;

import  java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet
{
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public Wallet()
    {
        generateKeyPair();
    }//end constructor Wallet

    public void generateKeyPair()
    {
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            //Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            //set the public and private keys from the keypair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

            }//end try
        catch (Exception e)
         {
             throw new RuntimeException(e);
         }//end catch
    }//end method generateKeyPair
}// end class Wallet
