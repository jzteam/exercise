package cn.jzteam.algorithm.leetcode.question1248;

/**
 * 给你一个整数数组 nums 和一个整数 k。
 *
 * 如果某个 连续 子数组中恰好有 k 个奇数数字，我们就认为这个子数组是「优美子数组」。
 *
 * 请返回这个数组中「优美子数组」的数目。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：nums = [1,1,2,1,1], k = 3
 * 输出：2
 * 解释：包含 3 个奇数的子数组是 [1,1,2,1] 和 [1,2,1,1] 。
 * 示例 2：
 *
 * 输入：nums = [2,4,6], k = 1
 * 输出：0
 * 解释：数列中不包含任何奇数，所以不存在优美子数组。
 * 示例 3：
 *
 * 输入：nums = [2,2,2,1,2,2,1,2,2,2], k = 2
 * 输出：16
 *  
 *
 * 提示：
 *
 * 1 <= nums.length <= 50000
 * 1 <= nums[i] <= 10^5
 * 1 <= k <= nums.length
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/count-number-of-nice-subarrays
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 个人方案：双指针
    // 圈住k个奇数，端点各向两端延伸，发现连续的偶数的个数+1，求积，则是对应这k个奇数的优美子数组的个数
    // 继续再圈另外的k个奇数，循环累加
    public static int numberOfSubarrays(int[] nums, int k) {
        int[] data = new int[nums.length];
        int dataLength = 0;
        for (int i=0;i<nums.length;i++) {
            if (nums[i] % 2 == 1) {
                data[dataLength++] = i;
            }
        }
        int num = 0;
        int pos = 0;
        while (pos + k - 1 < dataLength) {
            // 两端偶数个数，0也是一种可能
            int leftNum = pos - 1 < 0 ? data[0] + 1 : data[pos] - data[pos-1];
            int rightNum = pos + k >= dataLength ? nums.length - data[dataLength-1] : data[pos + k] - data[pos + k - 1];
            num += leftNum * rightNum;
            pos++;
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println(numberOfSubarrays(new int[]{1,1,2,1,1}, 3));
        System.out.println(numberOfSubarrays(new int[]{2,4,6}, 1));
        System.out.println(numberOfSubarrays(new int[]{2,2,2,1,2,2,1,2,2,2}, 2));
        System.out.println(numberOfSubarrays(new int[]{1,1,1}, 2));
        System.out.println(numberOfSubarrays(new int[]{91473,45388,24720,35841,29648,77363,86290,58032,53752,87188,34428,85343,19801,73201}, 4));
    }
}
