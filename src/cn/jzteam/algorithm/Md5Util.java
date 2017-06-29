package cn.jzteam.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Md5Util {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        byte[] digest = instance.digest("jiankeyunwang".getBytes());
        System.out.println("digest:" + new String(digest));

        byte[] digest1 = instance.digest("jiankeyunwanf".getBytes());

        boolean equals = Arrays.equals(digest, digest1);
        System.out.println("equals:" + equals);


    }

}
