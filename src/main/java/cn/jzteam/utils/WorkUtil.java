package cn.jzteam.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkUtil {
    private static final String EMAIL_REGEXP = "^([a-zA-Z0-9_\\-\\.\\+]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,10}|[0-9]{1,3})(\\]?)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEXP);

    /**
     * email校验
     *
     * @param email
     * @return 格式是否正确
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        Matcher m = EMAIL_PATTERN.matcher(email);
        return m.matches();
    }

    /**
     * 获取昵称
     * 手机号大于7，显示前三后四,手机号小于等于7，前三位显示，后面加密；
     * 邮箱@前面小于3，不加密,大于等于3加密
     * 
     * @param nikeName
     * @return
     */
    public static String convertNickNme(String nikeName) {
        String result = nikeName;
        if (StringUtils.isNumeric(nikeName)) {
            if (nikeName.length() > 7) {
                result = nikeName.substring(0, 3) + "***" + nikeName.substring(nikeName.length() - 4);
            } else {
                result = nikeName.substring(0, 3) + "***";
            }
        } else {
            if (isEmail(nikeName)) {
                String emailPrefix = nikeName.split("@")[0];
                String emailTail = nikeName.split("@")[1];

                if (emailPrefix.length() > 3) {
                    result = emailPrefix.substring(0, 3) + "***@" + emailTail;
                } else {
                    result = nikeName;
                }
            }
        }

        return result;
    }
}
