package cn.jzteam.algorithm.leetcode.question0056;

import com.alibaba.fastjson.JSON;

import java.util.*;

public class Solution {

    // 个人方案，先按照区间起点进行排序
    // 然后循环一次，后者区间起点在前者之内（包含起终点），则使用两个区间终点最大值作为新区间终点，继续判断下一个区间
    public static int[][] merge(int[][] intervals) {
        if(intervals.length == 0) {
            return intervals;
        }
        // 先按照起点排序
        Map<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i<intervals.length; i++) {
            Integer old = map.get(intervals[i][0]);
            if (old != null) {
                map.put(intervals[i][0], Math.max(old, intervals[i][1]));
            } else {
                map.put(intervals[i][0], intervals[i][1]);
            }
        }

        int[][] result = new int[intervals.length][intervals[0].length];
        int pos = 0;

        int[] cur = intervals[0];
        Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
        Map.Entry<Integer, Integer> curEntry = iterator.next();
        cur[0] = curEntry.getKey();
        cur[1] = curEntry.getValue();
        while(iterator.hasNext()) {
            Map.Entry<Integer, Integer> next = iterator.next();
            if (next.getKey() <= cur[1]) {
                cur[1] = Math.max(next.getValue(), cur[1]);
            } else {
                result[pos++] = new int[]{cur[0], cur[1]};
                cur[0] = next.getKey();
                cur[1] = next.getValue();
            }
        }
        result[pos++] = new int[]{cur[0], cur[1]};
        return Arrays.copyOf(result, pos);
    }

    public static void main(String[] args) {
        int [][] t = {{1,3},{2,6},{8,10},{15,18}};
        System.out.println(JSON.toJSONString(merge(t)));
        System.out.println(JSON.toJSONString(merge(new int[][]{{2,3},{5,5},{2,2},{3,4},{3,4}})));
    }

}
