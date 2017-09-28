package cn.jzteam.test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArrayTest {
	
    public static void main(String[] args) {
        int[] a = new int[] { 1, 2, 3, 4, 5, 6, 7 };
        int[] b = { 8, 9 };
        String w = "2";
        String[] ss = { w };

        System.out.println(ss.length);
        System.arraycopy(a, 4, b, 1, 1);

        System.out.println(Arrays.toString(b));

        String string;
        try {
            string = new String("å¥æ³¢å¿ç".getBytes("Unicode"), "utf-8");
            System.out.println(string);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
