package cn.jzteam.core.grammar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@RunWith(JUnit4.class)
public class TestDateFormat {

    @Test
    public void test1() {
        // 默认时区
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        System.out.println(sdf.format(new Date(1650816000000L)));
        // 指定时区
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(sdf.format(new Date(1650816000000L)));
    }
}
