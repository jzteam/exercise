package cn.jzteam.algorithm.leetcode.question0053;

/**
 * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 *
 * 示例:
 *
 * 输入: [-2,1,-3,4,-1,2,1,-5,4],
 * 输出: 6
 * 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
 * 进阶:
 *
 * 如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的分治法求解。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximum-subarray
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // O(n) 起点一定是>0的序列
    public int maxSubArray(int[] nums) {
        int maxSum = Integer.MIN_VALUE;
        int temp = 0;
        for (int i=0;i<nums.length;i++) {
            temp += nums[i];
            if (temp > maxSum) {
                maxSum = temp;
            }
            if (temp <= 0) {
                temp = 0;
            }
        }
        return maxSum;
    }

    // 分治
    // 最大子序列在左侧
    // 最大子序列在右侧
    // 最大子序列在中间：固定了起点或终点，直接取两端最大值然后加和
    public static int maxSubArray1(int[] nums) {
        return dg(nums, 0, nums.length-1);
    }

    public static int dg (int[] nums, int start, int end) {
        if (start == end) {
            return nums[start];
        }
        int mid = (start + end) / 2;
        // 左侧
        int left = dg(nums, start, mid);
        // 右侧
        int right = dg(nums, mid+1, end);
        // 中间
        int leftMax = Integer.MIN_VALUE;
        int tempLeft = 0;
        int leftPos = mid;
        int rightMax = Integer.MIN_VALUE;
        int tempRight = 0;
        int rightPos = mid+1;
        while (leftPos >= 0) {
            tempLeft += nums[leftPos];
            if (tempLeft > leftMax) {
                leftMax = tempLeft;
            }
            leftPos--;
        }
        while (rightPos <= end) {
            tempRight += nums[rightPos];
            if (tempRight > rightMax) {
                rightMax = tempRight;
            }
            rightPos++;
        }
        // 取三者中最大值
        return Math.max(Math.max(left, right), leftMax + rightMax);
    }

    public static void main(String[] args) {
        System.out.println(maxSubArray1(new int[]{-2,1,-3,4,-1,2,1,-5,4})); // 6
        System.out.println(maxSubArray1(new int[]{-2}));
    }
}
