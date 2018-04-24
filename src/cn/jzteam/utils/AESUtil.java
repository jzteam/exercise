/**
 * @title AESUtil
 * @package com.okcoin.ucenter.users.util
 * @description: OKCoin
 * @copyright: Copyright (c) 2017
 * @company:北京乐酷达网络科技有限公司
 * @author taijun.li
 * @date 2018/3/29 18:50
 */
package cn.jzteam.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

//@Component
public class AESUtil {
    // 使用配置的方式，一定不能随意修改该key，否则会导致解密失败 "<<<!@#$%^&*()>>>"
    private static String key = "<<<!@#$%^&*()abc";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

    // 从配置中获取key，冒号不能去掉，用于分隔key与默认值。默认值为空串，避免yml没有配置key启动报错
    //@Value("${okcoin.users.encode.kycEncryptKey:}")
    public void setKey(final String encryptKey) {
        AESUtil.key = encryptKey;
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @return String 加密后字符串
     */
    public static String encrypt(final String content) {
        try {
            if (AESUtil.key == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (AESUtil.key.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            final byte[] raw = AESUtil.key.getBytes("utf-8");
            final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            // "算法/模式/补码方式"
            final Cipher cipher = Cipher.getInstance(AESUtil.DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            final byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (final Exception e) {
            //Logs.geterrorLogger().error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @return String  解密后的字符串
     */
    public static String decrypt(final String content) {
        try {
            // 判断Key是否正确
            if (AESUtil.key == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (AESUtil.key.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            final byte[] raw = AESUtil.key.getBytes("utf-8");
            final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            final Cipher cipher = Cipher.getInstance(AESUtil.DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            // 先用base64解密
            final byte[] encrypted1 = Base64.getDecoder().decode(content);
            final byte[] original = cipher.doFinal(encrypted1);
            final String originalString = new String(original, "utf-8");
            return originalString;

        } catch (final Exception e) {
        }
        return null;
    }


    public static void main(final String[] args) {
        // 需要加密的字串
        // final String str = "AESUtil-128/192/256 for  Social number, Passport number, Driver license number, Bank account number, Credit card number, users' name, phone number, e-mail address -> privacy law ";
        final String str = "MC0GCCqGSIb3DQIJAyEAh3jlrhteHHzIkN4cql+5Fyszqzpyd21leBuDwBUDvwM=";
        // final String str = "{\"birthDay\":\"19771109\",\"certMet\":\"M\",\"certNum\":\"48\",\"ci\":\"4whQvrZJj6/oJxOPsBLtNzNvoK4P49V20/frKyeYSMGT/EtCS8oZuo18fLO247FmIiEFNOIcP+Z+rJ43v4g/xg==\",\"date\":\"20180123172523\",\"di\":\"MC0GCCqGSIb3DQIJAyEAh3jlrhteHHzIkN4cql+5Fyszqzpyd21leBuDwBUDvwM=\",\"gender\":\"0\",\"ip\":\"106.255.247.162\",\"mBirthDay\":\"\",\"mGender\":\"\",\"mName\":\"\",\"mNation\":\"\",\"name\":\"윤여훈\",\"nation\":\"0\",\"phoneCorp\":\"LGT\",\"phoneNo\":\"01029595802\",\"plusInfo\":\"\",\"result\":\"Y\"}";
        System.out.println(str);
        // 加密
        final String enStr = AESUtil.encrypt("91520103MA6DMW4TX0");
        System.out.println("加密后的字串是：" + enStr);
        System.out.println("加密后的字串是：" + enStr.length());

        // 解密
        final String deStr = AESUtil.decrypt(enStr);
        System.out.println("解密后的字串是：" + deStr);
        System.out.println("解密后的字串是：" + deStr.length());
    }
}