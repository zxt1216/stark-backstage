package com.sean.arya.stark.backstage.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Sean
 * @date 2018/5/10 14:06
 * @description
 * @vesion 1.0.0
 */
public class RSA {

    private static final String CHARSET="UTF-8";
    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHM = "SHA1withRSA";

    /**
     * 1024密钥RSA加密最大分段大小.最大值不超过117
     */
    private static final int ENCRYPT_BLOCK = 64;

    /**
     * 1024密钥RSA解密最大分段大小,最大值不超过245
     */
    private static final int DECRYPT_BLOCK = 128;
    private RSAKey rsaKey;
    public RSA(RSAKey rsaKey){
        this.rsaKey = rsaKey;
    }

    /**
     * 公钥加密
     * @param src
     * @return
     */
    public String encrypt(String src)throws Exception{
        final Cipher cipher = Cipher.getInstance(rsaKey.getRSAPublicKey().getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, rsaKey.getRSAPublicKey());
        byte[] segments=segmentDeal(cipher,ENCRYPT_BLOCK,src.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(segments);
    }
    /**
     * 私钥解密
     * @param src
     * @return
     */
    public String decrypt(String src)throws Exception{
        final Cipher cipher = Cipher.getInstance(rsaKey.getRSAPrivateKey().getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, rsaKey.getRSAPrivateKey());
        byte[] segments=segmentDeal(cipher,DECRYPT_BLOCK,Base64.getDecoder().decode(src));
        return new String(segments,CHARSET);
    }

    /**
     * 签名
     * @param src 需要签名的字符串
     * @return
     */
    public String sign(String src,String charset)throws Exception{
        Signature signature=Signature.getInstance(SIGN_ALGORITHM);
        signature.initSign(rsaKey.getRSAPrivateKey());
        signature.update(src.getBytes(StringUtils.isEmpty(charset)?CHARSET:charset));
        byte[] signs=signature.sign();
        return Base64.getEncoder().encodeToString(signs);
    }

    /**
     *
     * @param  sign 签名
     * @param ref 原始数据
     * @return
     * @throws Exception
     */
    public boolean verify(String sign,String ref,String charset)throws Exception{
        Signature signature=Signature.getInstance(SIGN_ALGORITHM);
        signature.initVerify(rsaKey.getRSAPublicKey());
        signature.update(ref.getBytes(StringUtils.isEmpty(charset)?CHARSET:charset));
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    private byte[] segmentDeal(Cipher cipher, int block, byte[] data) throws Exception {
        final int len = data.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // 对数据分段加密或解密
            for (int offSet=0;offSet<len;offSet +=block) {
                final byte[] cache;
                if (len>=block+offSet) {
                    cache = cipher.doFinal(data,offSet,block);
                } else {
                    cache = cipher.doFinal(data,offSet,len-offSet);
                }
                out.write(cache,0,cache.length);
            }
            return out.toByteArray();
        }
    }

    public static class RSAKey{
        private String keyFileName;
        private String priKey;
        private String pubKey;
        public RSAKey(String keyFileName, String pubKey, String priKey){
            this.keyFileName=keyFileName;
            this.priKey=priKey;
            this.pubKey=pubKey;
        }
        public RSAPrivateKey getRSAPrivateKey()throws NoSuchAlgorithmException, InvalidKeySpecException {
            String priKStr=ResourceBundle.getBundle(keyFileName).getString(priKey).trim();
            return (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(priKStr)));

        }
        public RSAPublicKey getRSAPublicKey()throws NoSuchAlgorithmException, InvalidKeySpecException {
            String pubKStr=ResourceBundle.getBundle(keyFileName).getString(pubKey).trim();
            return (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(pubKStr)));
        }
        public static Map<String,String> keyGen()throws NoSuchAlgorithmException, InvalidKeySpecException,UnsupportedEncodingException {
            Map<String,String> keyMap = new HashMap<String,String>();
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
            keygen.initialize(1024, random);
            // 取得密钥对
            KeyPair kp = keygen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey)kp.getPrivate();
            String privateKeyString = new String(Base64.getEncoder().encode(privateKey.getEncoded()));
            RSAPublicKey publicKey = (RSAPublicKey)kp.getPublic();
            String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
            keyMap.put("PUBLIC_KEY", publicKeyString);
            keyMap.put("PRIVATE_KEY", privateKeyString);
            return keyMap;
        }
    }
    public static void main(String[] a)throws Exception{
        RSA rsa=new RSA(new RSA.RSAKey("rsa","pub","pri"));
        String data="wgnhfkdafngvgsokdafrkgfmkl";
        String en=rsa.encrypt(data);
        String de=rsa.decrypt(en);
        String sign=rsa.sign(data,null);
        boolean ver=rsa.verify(sign,data,null);
        System.out.println(de.equals(data));
    }
}
