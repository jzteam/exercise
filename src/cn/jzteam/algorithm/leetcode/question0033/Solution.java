package cn.jzteam.algorithm.leetcode.question0033;

import com.alibaba.fastjson.JSON;

public class Solution {

    // O(log n)就表示要用二分
    // 二分需要有序数组，这个旋转过后的数组，一定会有一部分是无序的，怎么判断呢：
    // 有序的那部分一定满足 第一个元素小于最后一个元素
    public static int search(int[] nums, int target) {
        if (nums.length == 0) {
            return -1;
        }
        return binarySearch(nums, 0, nums.length - 1, target);
    }

    private static int binarySearch(int[] nums, int start, int end, int target) {
        if (start == end) {
            return nums[start] == target ? start : -1;
        }
        int mid = (start + end) / 2;
        if (nums[start] < nums[mid]) {
            // 左半部分是有序的，先判断target在不在这里
            if (target <= nums[mid] && target >= nums[start]) {
                // 在左边
                return binarySearch(nums, start, mid, target);
            } else {
                // 在右边
                return binarySearch(nums, mid + 1, end, target);
            }
        } else {
            // 右半部分是有序的，先判断target在不在这里
            if (target >= nums[mid + 1] && target <= nums[end]) {
                // 在右边
                return binarySearch(nums, mid + 1, end, target);
            } else {
                // 在左边
                return binarySearch(nums, start, mid, target);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(search(new int[]{4,5,6,7,0,1,2}, 0)));
        System.out.println(JSON.toJSONString(search(new int[]{4,5,6,7,0,1,2}, 3)));
    }
}
