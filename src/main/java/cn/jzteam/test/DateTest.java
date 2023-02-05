package cn.jzteam.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTest {

    public static void main(String[] args) throws ParseException {
        printSysProperties();
        System.out.println("当前默认时区id："+TimeZone.getDefault().getID());
        System.out.println("当前默认时区id："+Calendar.getInstance().getTimeZone().getID());

        String hanguo = "Asia/Seoul";
        String shanghai = "Asia/Shanghai";

        System.out.println("上海时区id:"+TimeZone.getTimeZone(shanghai).getID());
        System.out.println("韩国时区id:"+TimeZone.getTimeZone(hanguo).getID());

        final Date date = new Date();

        System.out.println("默认毫秒："+date.getTime());

        System.setProperty("user.timezone", "Asia/Seoul");
        System.out.println("01毫秒："+date.getTime());
        System.out.println("02毫秒："+new Date().getTime());
        System.out.println("03时区id："+TimeZone.getDefault().getID());


        final Calendar instance = Calendar.getInstance();
        System.out.println("04:"+instance.getTimeZone().getID());

        printSysProperties();

        instance.setTimeZone(TimeZone.getTimeZone(hanguo));
        final Date hanguoDate = instance.getTime();

        final SimpleDateFormat formatterShanghai = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
        final SimpleDateFormat formatterHanguo = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);

        final String shanghaiStr = formatterShanghai.format(date);
        final String hanguoStr = formatterHanguo.format(hanguoDate);

        System.out.println("上海："+shanghaiStr+"，韩国："+hanguoStr);

    }


    public static void printSysProperties() {
        Properties props = System.getProperties();
        Iterator iter = props.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if(key.equals("user.timezone"))
            System.out.println(key + " = " + props.get(key));

        }

    }
}
