package cn.jzteam.algorithm.leetcode.question0055;

/**
 * 给定一个非负整数数组，你最初位于数组的第一个位置。
 *
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 *
 * 判断你是否能够到达最后一个位置。
 *
 * 示例 1:
 *
 * 输入: [2,3,1,1,4]
 * 输出: true
 * 解释: 我们可以先跳 1 步，从位置 0 到达 位置 1, 然后再从位置 1 跳 3 步到达最后一个位置。
 * 示例 2:
 *
 * 输入: [3,2,1,0,4]
 * 输出: false
 * 解释: 无论怎样，你总会到达索引为 3 的位置。但该位置的最大跳跃长度是 0 ， 所以你永远不可能到达最后一个位置。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/jump-game
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // 贪心算法
    // 记录一个能达到最远的位置索引
    // 只要在这个位置索引之前，还能更新最远位置，那么就能继续下去
    // 当最远位置>=数组长度-1，直接返回true
    public static boolean canJump(int[] nums) {
        int maxIndex = 0;
        // 循环条件是基于上一次计算结果
        for (int i = 0;i<=maxIndex;i++) {
            if ((maxIndex = Math.max(maxIndex, i+nums[i])) >= nums.length-1) {
                return true;
            }
        }
        return false;
    }


    // 个人方案
    // f[i] = i + data[i] >= minCanJump
    public static boolean canJump1(int[] nums) {
        int minCanJump = nums.length - 1;
        for (int i = nums.length-1; i>=0; i--) {
            if (nums[i] + i >= minCanJump) {
                minCanJump = i;
            }
        }
        return minCanJump == 0;
    }

    public static void main(String[] args) {
        System.out.println(canJump(new int[]{2,3,1,1,4}));
        System.out.println(canJump(new int[]{3,2,1,0,4}));
    }
}
