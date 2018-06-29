package muokiBlockChainWallet;
import java .security.*;
import  java.util.ArrayList;
import java.util.Base64;
import com.google.gson.GsonBuilder;
import java.util.List;

import java.util.List;

public class StringUtil
{
    //applies sha256 to a string and returns the ressult
    public static String applySha256(String input)
    {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            //Applies sha256 to our input
            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            StringBuffer hexString = new StringBuffer();//This will contain hash as hexidecimal
            for(int i = 0; i< hash.length; i++)
            {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }//end for
            return hexString.toString();

        }//end try
        catch (Exception e){

            throw new RuntimeException(e);

        }//end catch
    }//end method applySha256

    //Applies ECDSA Signature and returns the result(as bytes).
    public static byte[] applyECDSASig(PrivateKey privateKey, String input)
    {
        Signature dsa;
        byte[] output = new byte[0];
        try{
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        }//end try
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }//end catch
        return output;
    }//end applyECDSASig

    //Verifies a String signature
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }//end try
        catch(Exception e) {
            throw new RuntimeException(e);
        }//end catch
    }//end class verifyECDSASig

    //Short hand helper to turn Object into a json string
    public static String getJson(Object o)
    {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }//end getJson

    //Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"
    public static String getDificultyString(int difficulty)
    {
        return new String(new char[difficulty]).replace('\0', '0');
    }//end getDificultyString

    public static String getStringFromKey(Key key)
    {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }// end getStringFromKey

    public static String getMerkleRoot(ArrayList<Transaction> transactions)
    {
        int count = transactions.size();

        List<String> previousTreeLayer = new ArrayList<String>();
        for(Transaction transaction : transactions) {
            previousTreeLayer.add(transaction.transactionID);
        }//end for
        List<String> treeLayer = previousTreeLayer;

        while(count > 1) {
            treeLayer = new ArrayList<String>();
            for(int i=1; i < previousTreeLayer.size(); i+=2) {
                treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
            }//end for
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }//end while

        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }//end getMerkleRoot
}// end class StringUtil
