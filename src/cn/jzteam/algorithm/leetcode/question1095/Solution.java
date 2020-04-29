package cn.jzteam.algorithm.leetcode.question1095;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 *  山脉数组中查找目标值
 * （这是一个 交互式问题 ）
 *
 * 给你一个 山脉数组 mountainArr，请你返回能够使得 mountainArr.get(index) 等于 target 最小 的下标 index 值。
 *
 * 如果不存在这样的下标 index，就请返回 -1。
 *
 *  
 *
 * 何为山脉数组？如果数组 A 是一个山脉数组的话，那它满足如下条件：
 *
 * 首先，A.length >= 3
 *
 * 其次，在 0 < i < A.length - 1 条件下，存在 i 使得：
 *
 * A[0] < A[1] < ... A[i-1] < A[i]
 * A[i] > A[i+1] > ... > A[A.length - 1]
 *  
 *
 * 你将 不能直接访问该山脉数组，必须通过 MountainArray 接口来获取数据：
 *
 * MountainArray.get(k) - 会返回数组中索引为k 的元素（下标从 0 开始）
 * MountainArray.length() - 会返回该数组的长度
 *  
 *
 * 注意：
 *
 * 对 MountainArray.get 发起超过 100 次调用的提交将被视为错误答案。此外，任何试图规避判题系统的解决方案都将会导致比赛资格被取消。
 *
 * 为了帮助大家更好地理解交互式问题，我们准备了一个样例 “答案”：https://leetcode-cn.com/playground/RKhe3ave，请注意这 不是一个正确答案。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：array = [1,2,3,4,5,3,1], target = 3
 * 输出：2
 * 解释：3 在数组中出现了两次，下标分别为 2 和 5，我们返回最小的下标 2。
 * 示例 2：
 *
 * 输入：array = [0,1,2,4,2,1], target = 3
 * 输出：-1
 * 解释：3 在数组中没有出现，返回 -1。
 *  
 *
 * 提示：
 *
 * 3 <= mountain_arr.length() <= 10000
 * 0 <= target <= 10^9
 * 0 <= mountain_arr.get(index) <= 10^9
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-in-mountain-array
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 个人方案：二分 + 递归
    // 递归方法每次取中间两个数，能判断此位于上升坡还是下降坡，同时将山脉数组分成左右两个数组，一个是山脉数组，另一个是有序数组。
    // 判断target是否存在于有序数组：
    //      如果左边是有序数组，二分查找target，找到了则一定是最小index，直接返回；如果没找到则递归右边的山脉数组
    //      如果左边是山脉数组，递归查找山脉数组，找到了直接返回，没找到再二分查找右边的有序数组
    public static int findInMountainArray(int target, List<Integer> mountainArr) {
        return dg(target, mountainArr, 0, mountainArr.size()-1);
    }

    private static int dg(int target, List<Integer> mountainArray, int start, int end) {
        // 每次分割两组，最小时可能length=1，一个length=2，所以两种特殊情况都要判断
        if (start == end) {
            if (mountainArray.get(start) == target) {
                return start;
            } else {
                return -1;
            }
        }

        // 取中间部分，并判断
        int mid = (start + end) / 2;
        int val1 = mountainArray.get(mid);
        int val2 = mountainArray.get(mid+1);
        int midResult = -1;
        if (val1 == target) midResult = mid;
        if (val2 == target) midResult = mid+1;

        // 中间部分已经get过了，不能重复get
        if (val1 < val2) {
            // 优先左边结果：左边是单调递增
            if (start < mid && target < val1) {
                int left = binary(target, mountainArray, start, mid-1, true);
                if (left != -1) {
                    return left;
                }
            }
            // 其次中间结果
            if (midResult != -1) {
                return midResult;
            }
            // 最后用右边结果：右边是山脉
            if (mid + 1 < end) {
                return dg(target, mountainArray, mid+2, end);
            }
        } else {
            // 优先左边结果：左边是山脉
            if (start < mid) {
                int left = dg(target, mountainArray, start, mid - 1);
                if (left != -1) {
                    return left;
                }
            }
            // 其次中间结果
            if (midResult != -1) {
                return midResult;
            }
            // 最后用右边结果：右边是单调递减
            if (mid + 1 < end && target < val2) {
                return binary(target, mountainArray, mid + 2, end, false);
            }
        }

        return -1;
    }
    private static int binary (int target, List<Integer> mountainArray, int start, int end, boolean asc) {
        if (start >= end) {
            if (mountainArray.get(start) == target) {
                return start;
            } else {
                return -1;
            }
        }
        int mid = (start + end) / 2;
        Integer midValue = mountainArray.get(mid);
        if (midValue == target) {
            return mid;
        }
        if (asc) {
            if (midValue < target) {
                return binary(target, mountainArray, mid+1, end, asc);
            } else if (start < mid){
                return binary(target, mountainArray, start, mid-1, asc);
            }
        } else {
            if (mountainArray.get(mid) < target && start < mid) {
                return binary(target, mountainArray, start, mid-1, asc);
            } else {
                return binary(target, mountainArray, mid+1, end, asc);
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(findInMountainArray(3, Lists.newArrayList(1,2,3,4,5,3,1)));
        System.out.println(findInMountainArray(3, Lists.newArrayList(0,1,2,4,2,1)));
    }
}
