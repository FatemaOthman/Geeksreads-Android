/*
 * This class has some public helping functions that can be used in the code of many other classes
 */
package CustomFunctions;
import android.content.Context;
import android.content.Intent;

import com.example.geeksreads.FeedActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HelpingFunctions {
    /* Function that encrypts input string into MD5 Format */
    public static String getMD5Encryption(String inputStr)
    {
        /* Variables Initialization */
        MessageDigest md5 = null;
        BigInteger md5Data = null;

        /* Converts input string into an array of bytes */
        byte[] data = inputStr.getBytes();

        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(data);
            md5Data = new BigInteger(1, md5.digest());
            return md5Data.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return inputStr;
        }
    }
}
