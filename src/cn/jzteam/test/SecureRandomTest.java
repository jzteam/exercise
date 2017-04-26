package cn.jzteam.test;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SecureRandomTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // SecureRandom secureRandom = SecureRandom.getInstance("MD5");
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 10000; i++) {

            int result = 6 + (int) (Math.random() * 7);
            // System.out.println("6-12随机数：" + result);
            switch (result) {
                case 6:
                    map.put(0, map.get(0) == null ? 0 : map.get(0) + result);
                    break;
                case 7:
                    map.put(1, map.get(1) == null ? 0 : map.get(1) + result);
                    break;
                case 8:
                    map.put(2, map.get(2) == null ? 0 : map.get(2) + result);
                    break;
                case 9:
                    map.put(3, map.get(3) == null ? 0 : map.get(3) + result);
                    break;
                case 10:
                    map.put(4, map.get(4) == null ? 0 : map.get(4) + result);
                    break;
                case 11:
                    map.put(5, map.get(5) == null ? 0 : map.get(5) + result);
                    break;
                case 12:
                    map.put(6, map.get(6) == null ? 0 : map.get(6) + result);
                    break;
                default:
                    break;
            }
        }

        System.out.println(map);

    }
}
