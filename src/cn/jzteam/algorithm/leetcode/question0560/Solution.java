package cn.jzteam.algorithm.leetcode.question0560;

import java.util.HashMap;
import java.util.Map;

/** 和为k的子数组
 * 给定一个整数数组和一个整数 k，你需要找到该数组中和为 k 的连续的子数组的个数。
 *
 * 示例 1 :
 *
 * 输入:nums = [1,1,1], k = 2
 * 输出: 2 , [1,1] 与 [1,1] 为两种不同的情况。
 * 说明 :
 *
 * 数组的长度为 [1, 20,000]。
 * 数组中元素的范围是 [-1000, 1000] ，且整数 k 的范围是 [-1e7, 1e7]。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/subarray-sum-equals-k
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // 和为key，hashMap
    public static int subarraySum(int[] nums, int k) {
        // 逐个位置求和，并将和作为key放入map中，只要当前位置的和-k，得到的结果在map中查到数量，全局累加此数量
        // 以 j 为终点的子数组满足和为k，那么它的起点i前一个位置的和一定满足：Sum = Sum_j - k
        int count = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(nums[0], 1);
        if (nums[0] == k) {
            count++;
        }
        for (int i=1;i<nums.length;i++) {
            nums[i] = nums[i-1] + nums[i];
            if (nums[i] == k) {
                // 起点的前一个位置不存在，不能从map中获取
                // 如果在map增加一个特殊值 0-1，那么当k=0时，就会额外多一个1
                count++;
            }
            // 当前位置为终点，那么起点只能取值 0～i。而起点前一个位置的和有两个特殊情况：
            // 0的前一个位置不存在，此状态就是 从数组第一个位置到此处的和，nums[i]来判断即可
            // i的前一个位置，那么当前位置的和nums[i]暂时不能放入map，所以先从map中取  nums[i]-k，然后再把nums[i]放入map
            Integer r = map.get(nums[i] - k);
            if (r != null) {
                count += r;
            }
            Integer integer = map.get(nums[i]);
            if (integer == null) {
                map.put(nums[i], 1);
            } else {
                map.put(nums[i], integer+1);
            }
        }
        return count;
    }

    // 二维数组：i为起点，j为终点，值为和，时间复杂度 O(n*n)
    // 内存超出限制
    public static int subarraySum1(int[] nums, int k) {
        int num = 0;
        int[][] data = new int[nums.length][nums.length];
        // 计算所有起点-终点的和
        for (int i=0;i<nums.length;i++) {
            for(int j=i;j<nums.length;j++) {
                data[i][j] = (j==i?0:data[i][j-1]) + nums[j];
                if (data[i][j] == k) {
                    num++;
                }
            }
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println(subarraySum(new int[]{1}, 0));
        System.out.println(subarraySum(new int[]{1}, 1));
        System.out.println(subarraySum(new int[]{1, 2, 3}, 3));
        System.out.println(subarraySum(new int[]{-1, -1, 1}, 0));
    }
}
