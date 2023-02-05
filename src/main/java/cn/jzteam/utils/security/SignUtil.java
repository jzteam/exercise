package cn.jzteam.utils.security;


import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 签名工具，使用SHA256算法
 */
public class SignUtil {

    // 签名对应的参数名称，随请求一起发送
    public final static String SIGN_PARAM = "sign";
    // key对应的参数名称，随请求一起发送
    public final static String CLIENT_KEY = "client_key";
    // 签名拼串时secret的名称，不随请求发送
    public final static String CLIENT_SECRET = "client_secret";

    /**
     * 签名，不变动任何入参
     * @param map 请求所有参数
     * @param key 用户标记，区分是哪个用户，随请求一起传输
     * @param secret 用户安全密钥，用于拼串后签名，但不随请求传输
     * @return
     */
    public static String getSign(Map<String, String> map, String key, String secret) {
        if(map == null || StringUtils.isEmpty(key) || StringUtils.isEmpty(secret)){
            throw new RuntimeException("参数不正确");
        }

        // 所有参数解析成列表，同时获取参数client_key
        List<String> list = new ArrayList();
        Iterator var3 = map.entrySet().iterator();
        while(var3.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var3.next();
            // 过滤掉 sign参数，client_key参数 和 空值参数，收集参数键值对
            if (!CLIENT_KEY.equals(entry.getKey()) && !SIGN_PARAM.equals(entry.getKey()) && !StringUtils.isEmpty(entry.getValue())) {
                list.add(entry.getKey() + "=" + entry.getValue());
            }
        }

        // 参数列表中增加指定的client_key：使用指定的key而不是用map中的client_key，可让方法通用于签名和验签
        list.add(CLIENT_KEY + "=" + key);

        // 排序：参数名按照字母序，区分大小写
        int size = list.size();
        String[] array = list.toArray(new String[size]);
        Arrays.sort(array, String.CASE_INSENSITIVE_ORDER);

        // 拼接字符串
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < size; ++i) {
            builder.append(array[i]).append("&");
        }

        // 拼上secret：secret不能当作请求参数发送出去了
        builder.append(CLIENT_SECRET + "=").append(secret);
        String result = builder.toString();
        result = EncryptUtil.getSHA256String(result);
        return result;
    }
}
