package cn.jzteam.base;

import java.math.BigDecimal;

public class TestBigDecimal {

    public static void main(String[] args) {
//        float a = 1.0f;
//        float b = 0.01f;
//        System.out.println(a - b);
//
        try
        {
            Double d = null;
            // 空指针异常
            System.out.println(0.5 + d);
            System.out.println(BigDecimal.valueOf(d).doubleValue());
        
        
//            throw new RuntimeException();
        }finally
        {
//            return;
        }
    }

}
