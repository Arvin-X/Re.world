package com.reuworld.reworld.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Arvin.X on 16/5/2.
 * encryption helper
 */
public class EncryptionTool {
    private static String TAG="--EncryptionTool--:";
    private static String salt1="re$";
    private static String salt2="RE#";
    private static String ecryptionType="SHA-1";

    /**
     * Hash encryption, not recommended, see getSHA1_HMAC
     * @param val wait to encrypt
     * @return sha1 hash value
     */
    public static String getSHA1(String val){

        try{
            MessageDigest md5 = MessageDigest.getInstance(ecryptionType);
            md5.update(val.getBytes());
            byte[] m = md5.digest();
            return getString(m);
        }
        catch (NoSuchAlgorithmException e) {
            Log.i(TAG, "getSHA: exception: " + e.toString());
            return null;
        }
    }

    /**
     * Hash encryption, use HMAC encryption
     * @param val wait to encrypt
     * @return HMAC value use SHA1
     */
    public static String getSHA1_HMAC(String val){
        return getSHA1(getSHA1(val+salt1)+salt2);
    }

    private static String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }

    public static String img2Str(Bitmap bitmap){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);
        byte[] bytes = b.toByteArray();

        //base64 encode
        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
        String encodeString = new String(encode);
        return encodeString;
    }

    public static Bitmap str2Img(String s){
        byte[] decode = Base64.decode(s,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }


}
