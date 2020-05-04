package cn.jzteam.algorithm.leetcode.question0045;

import sun.nio.cs.ext.MacHebrew;

/**
 * 给定一个非负整数数组，你最初位于数组的第一个位置。
 *
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 *
 * 你的目标是使用最少的跳跃次数到达数组的最后一个位置。
 *
 * 示例:
 *
 * 输入: [2,3,1,1,4]
 * 输出: 2
 * 解释: 跳到最后一个位置的最小跳跃数是 2。
 *      从下标为 0 跳到下标为 1 的位置，跳 1 步，然后跳 3 步到达数组的最后一个位置。
 * 说明:
 *
 * 假设你总是可以到达数组的最后一个位置。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/jump-game-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 个人方案
    // 每次找到可以到达最远的地方，如果足以达到目标，则表示此时是最快的方式
    // 贪心算法，通过局部最优解得到全局最优解。
    public static int jump(int[] nums) {
        int start = 0;
        int end = 0;
        int stepMax = nums[0];
        int count = 0;
        while (start <= end && end < nums.length-1) {
            stepMax = Math.max(start + nums[start], stepMax);
            if (start == end) {
                end = stepMax; // end变化一次表示走了一步
                count++;
            }
            start++;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(jump(new int[]{2,3,1,1,4})); // 2
        System.out.println(jump(new int[]{2,3})); // 2
    }
}
