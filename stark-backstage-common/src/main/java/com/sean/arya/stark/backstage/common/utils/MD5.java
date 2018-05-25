package com.sean.arya.stark.backstage.common.utils;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author Sean
 * @date 2018/5/10 19:31
 * @description MD5加密
 * @vesion 1.0.0
 */
public class MD5 {
    private static final String CHARSET="UTF-8";
    private MD5(){}
    /**
     * 加密
     * @param src 加密字段
     * @param salt 掩码
     * @return
     * @throws Exception
     */
    public static String encrypt(String src,String salt) throws Exception {
        return encrypt(src,salt,1);
    }
    public static String encrypt(String src,String salt,int count) throws Exception {
        byte[] bytes=new byte[]{};
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for(int i=0;i<count;i++) {
            bytes =md5.digest(src.concat(salt).getBytes(CHARSET));
            src=Base64.getEncoder().encodeToString(bytes);
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 校验
     * @param encry 加密后字段
     * @param src  加密原文
     * @param salt  掩码
     * @return
     * @throws Exception
     */
    public static boolean verify(String encry,String src,String salt,int count) throws Exception {
        return encry.equals(encrypt(src,salt,count));
    }
    public static boolean verify(String encry,String src,String salt) throws Exception {
        return encry.equals(encrypt(src,salt,1));
    }
    public static void main(String[] a)throws Exception{
        System.out.println(encrypt("xxx","salt"));
        System.out.println(verify("3X6YKNZM5l+GHTPP5hLj/Q==","xxx","salt"));
    }
}
