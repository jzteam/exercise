package cn.jzteam.core.collect;

import java.util.HashMap;
import java.util.Map;

public class TestMap {

    public static void main(String[] args) {
        /**
         * Long重写了hashCode方法，根据value计算的
         * 所以只要数值相同，hashCode也是相同的，map根据hashCode处理，也就是认为是同一个key了
         */
        Map<Long, String> map = new HashMap<>();
        map.put(new Long(111111L), "test");
        map.put(new Long(111111L), "test1");
        System.out.println(map.size());
    }
}
