package cn.jzteam.utils;

import com.alibaba.druid.util.StringUtils;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

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

    public static boolean isMobileByGoogle(int mobile, int areaCode) {
        final PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(areaCode);
        pn.setNationalNumber(mobile);

        return PhoneNumberUtil.getInstance().isValidNumber(pn);
    }

    public static void main(String[] args) {

        final PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(86);
        pn.setNationalNumber(19110246073L);

        final boolean mark = PhoneNumberUtil.getInstance().isValidNumber(pn);
        System.out.println("mark="+mark);
//        System.out.println(isMobile("19953817118"));
//        System.out.println(isMobile("+9999123456"));
    }
}
