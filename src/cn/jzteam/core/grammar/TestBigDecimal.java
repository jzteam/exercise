package cn.jzteam.core.grammar;

import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;

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
    	
    	test1();
    }
    
    private static void test1(){
    	String data = "2.33";
    	System.out.println(NumberUtils.isNumber(data));
    }

}
