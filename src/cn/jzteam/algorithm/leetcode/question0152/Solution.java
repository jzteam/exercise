package cn.jzteam.algorithm.leetcode.question0152;

/** 乘积最大子数组
 * 给你一个整数数组 nums ，请你找出数组中乘积最大的连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
 *
 *  
 *
 * 示例 1:
 *
 * 输入: [2,3,-2,4]
 * 输出: 6
 * 解释: 子数组 [2,3] 有最大乘积 6。
 * 示例 2:
 *
 * 输入: [-2,0,-1]
 * 输出: 0
 * 解释: 结果不能为 2, 因为 [-2,-1] 不是子数组。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximum-product-subarray
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // 题解：动态规划
    // 以i元素结尾的最大乘积为 f(i)_max = max{f(i-1)_max*a_i, f(i-1)_min*a_i, a_i}
    public static int maxProduct(int[] nums) {
        // 二维数组 0-最大，1-最小
        long[][] mm = new long[nums.length][2];
        // 初始化
        long max = mm[0][0] = mm[0][1] = nums[0];
        for (int i=1;i<nums.length;i++) {
            // 最大
            mm[i][0] = Math.max(Math.max(mm[i-1][0]*nums[i], mm[i-1][1]*nums[i]), nums[i]);
            // 最小
            mm[i][1] = Math.min(Math.min(mm[i-1][0]*nums[i], mm[i-1][1]*nums[i]), nums[i]);
            // 取最大
            max = Math.max(max, mm[i][0]);
        }
        return (int)max;
    }

    public static void main(String[] args) {
        System.out.println(maxProduct(new int[]{2,3,-2,4})); // 6
        System.out.println(maxProduct(new int[]{-2,0,-1})); // 0
        System.out.println(maxProduct(new int[]{-1,-2,-9,-6})); // 108
    }
}
