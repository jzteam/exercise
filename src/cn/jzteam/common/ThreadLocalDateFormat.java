package cn.jzteam.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {

    DateFormat proto;
    private static final String DATE_FORMAT = "yyyyMMddHHmmss";

    public ThreadLocalDateFormat() {
        super();
        SimpleDateFormat tmp = new SimpleDateFormat(DATE_FORMAT);
        proto = tmp;
    }

    public ThreadLocalDateFormat(String dataFormate) {
        super();
        SimpleDateFormat tmp = new SimpleDateFormat(dataFormate);
        proto = tmp;
    }

    @Override
    protected DateFormat initialValue() {
        return (DateFormat) proto.clone();
    }
}
