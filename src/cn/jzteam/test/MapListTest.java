package cn.jzteam.test;

import java.util.*;

public class MapListTest {

    private static List<String> list;
    private static Map<String, String> map;

    /**
     * 初始化n个数据
     * @param n
     */
    public static void init(int n) {
        list = new ArrayList<>();
        map = new HashMap<>(n);
        for (int i = 0 ;i<n;i++) {
            String temp = "test" + i;
            list.add(temp);
            map.put(temp, temp);
        }
    }

    /**
     * 从list中查找
     * @param key
     * @return
     */
    private static String getFromList (String key) {
        for (String s : list) {
            if (s != null && s.equalsIgnoreCase(key)) {
                return s;
            }
        }
        return null;
    }

    /**
     * 从map中查找
     * @param key
     * @return
     */
    private static String getFromMap (String key) {
        return map.get(key);
    }

    /**
     * 查找次数
     * 返回 map耗时 - list耗时
     * @param times
     */
    public static void findTimes(int num, int times) {
        // 准备随机数
        Random random = new Random();
        List<String> temp = new ArrayList<>();
        for (int i = 0; i< times;i++) {
            temp.add("test" + random.nextInt(num));
        }

        long start = System.currentTimeMillis();
        // 先测试 map
        for (String key : temp) {
            if(getFromMap(key) == null) {
                System.out.println("=========map失败=========================");
            }
        }
        long mapCost = System.currentTimeMillis() - start;
//        System.out.println("findTimes " + times + "次， mapCost = " + mapCost);
        start = System.currentTimeMillis();

        // 测试 list
        for (String key : temp) {
            if(getFromList(key) == null) {
                System.out.println("=========list失败=========================");
            }
        }
        long listCost = System.currentTimeMillis() - start;
//        System.out.println("findTimes " + times + "次， listCost = " + listCost);

        System.out.println("数据量为 " + num + " 时， 查询 " + times + " 次，map 耗时 " + mapCost + "ms， 比 list 快 " + (listCost-mapCost) + "ms");
    }

    public static void main(String[] args) {
        // 一般枚举状态，不超过10个
//        init (10);
//        findTimes(10, 2);

        for (int n = 1; n<=1000; n*=10) {
            init(n);
            for (int i = 1; i <= 10000000; i *= 10) {
                findTimes(n, i);
            }
            System.out.println();
        }

    }


}
