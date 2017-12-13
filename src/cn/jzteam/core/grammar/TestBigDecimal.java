package cn.jzteam.core.grammar;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

public class TestBigDecimal {

    public static void main(String[] args) {
//        float a = 1.0f;
//        float b = 0.01f;
//        System.out.println(a - b);
    	
//
//        try
//        {
//            Double d = null;
//            // 空指针异常
//            System.out.println(0.5 + d);
//            System.out.println(BigDecimal.valueOf(d).doubleValue());
//        
//        
////            throw new RuntimeException();
//        }finally
//        {
////            return;
//        }
    	
    	test2();
    }
    
    private static void test1(){
    	String data = "2.33";
    	System.out.println(NumberUtils.isNumber(data));
    }

    private static void test2(){
        BigDecimal bigDecimal = BigDecimal.valueOf(1234565433456765.345676543000000000D);
        System.out.println(bigDecimal.toString());
    }

}
