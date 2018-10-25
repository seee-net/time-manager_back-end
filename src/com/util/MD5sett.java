package com.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* MD5加盐加密 */
public class MD5sett {
    public static String setMD5( String salt, String purePassword){
        StringBuilder hidPassword= new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update( ( purePassword + salt ).getBytes());
            for (byte b:md5.digest()){
                hidPassword.append(String.format("%02X",b));
            }
            return hidPassword.toString();
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
}

