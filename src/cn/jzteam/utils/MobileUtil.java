package cn.jzteam.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileUtil {

    public static boolean isMobile(String mobiles) {
        if (StringUtils.isEmpty(mobiles)) {
            return false;
        } else {
            String regex1 = "^1[3|4|5|7|8|9][0-9]{9}$";
            String regex2 = "^\\+[0-9]{1,4}?\\d{6,11}$";
            Pattern p1 = Pattern.compile(regex1);
            Pattern p2 = Pattern.compile(regex2);
            Matcher m1 = p1.matcher(mobiles);
            Matcher m2 = p2.matcher(mobiles);
            return m1.matches() || m2.matches();
        }
    }

    public static void main(String[] args) {
        System.out.println(isMobile("19953817118"));
        System.out.println(isMobile("+9999123456"));
    }
}
