package cn.jzteam.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.jzteam.common.DateUtil;


public class FirstTest {

    public static void main(String[] args) {
        System.out.println("请看：" + "jzteam".substring(0, 0));

        String[] str = { "df", "22" };

        List<String> list = new ArrayList<>();
        list.add("ddf");


        System.out.println(Double.valueOf("44999") / 14);
        BigDecimal divide = BigDecimal.valueOf(44999).divide(BigDecimal.valueOf(14), 2, BigDecimal.ROUND_HALF_UP);
        System.out.println(divide.toString());

    }

    private static void get(long l) {
        System.out.println("test成功");
    }

    private static String getTestStr() {
        int i = 4;
        if (i > 3)
            throw null;
        return "test";
    }

    private void test1() {
        // 默认缓存一个小时
        int holdSeconds = 3600;
        try {
            // 计算当天剩余时间（00:00:00表示每天的开始）
            String currentDateTime = DateUtil.getCurrentDateTime();// yyyyMMddhhmmss
            System.out.println("当前时间：" + currentDateTime);
            Integer currentHour = Integer.parseInt(currentDateTime.substring(8, 10));
            Integer currentMin = Integer.parseInt(currentDateTime.substring(10, 12));
            Integer currentSecond = Integer.parseInt(currentDateTime.substring(12));
            holdSeconds = (24 * 3600 - 1) - currentHour * 3600 - currentMin * 60 - currentSecond;
        } catch (NumberFormatException e) {
            System.out.println("计算当天剩余时间出错");
        }
        System.out.println("holdSeconds=" + holdSeconds);
    }
}
