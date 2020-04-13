package cn.jzteam.algorithm.leetcode.question0004;

/**
 * 给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。
 *
 * 请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。
 *
 * 你可以假设 nums1 和 nums2 不会同时为空。
 *
 * 示例 1:
 *
 * nums1 = [1, 3]
 * nums2 = [2]
 *
 * 则中位数是 2.0
 * 示例 2:
 *
 * nums1 = [1, 2]
 * nums2 = [3, 4]
 *
 * 则中位数是 (2 + 3)/2 = 2.5
 *
 * 来源：力扣（Le  etCode）
 * 链接：https://leetcode-cn.com/problems/median-of-two-sorted-arrays
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    /*
        两个数组各设置一个游标，从游标取值比较，较小值处的游标+1，循环比较直到两个游标相加大于数组长度之和的一半
     */
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int midIndex = (nums1.length + nums2.length) / 2;
        boolean isEven = (nums1.length + nums2.length) % 2 == 0;
        if (isEven) {
            // 奇数直接取，偶数还要-1才是第一个中位数的索引，还要取第二中位数
            midIndex--;
        }
        if (nums1.length == 0) {
            // nums1 为空，直接从nums2取中位数
            return (nums2[midIndex] + nums2[midIndex + (isEven?1:0)]) / 2.0D;
        } else if (nums2.length == 0) {
            // nums2 为空，直接从nums1取中位数
            return (nums1[midIndex] + nums1[midIndex + (isEven?1:0)]) / 2.0D;
        }
        // 都不为空，分从两个数组中逐个取值。
        int totalIndex = 0;
        int index1 = 0; // 同时表示nums1进入重排序列的个数
        int index2 = 0; // 同时表示nums2进入重排序列的个数
        while (totalIndex++ <= midIndex) {
            // 拿出两个数据当前位置的值，进行比较，哪个小则哪个进位
            if(index1 < nums1.length && index2 < nums2.length) {
                if (nums1[index1] <= nums2[index2]) {
                    index1++;
                } else {
                    index2++;
                }
            } else if (index1 < nums1.length) {
                index1++;
            } else if (index2 < nums2.length) {
                index2++;
            } else {
                System.out.println("不可能");
            }
        }
        // 此时index1和index2都是未对比的
        int midValue;
        if (index1 == 0) {
            midValue = nums2[index2-1];
        } else if (index2 == 0) {
            midValue = nums1[index1-1];
        } else {
            // 已经进入重拍序列的，取最右侧的值，即较大值
            midValue = nums1[index1 - 1] > nums2[index2 - 1] ? nums1[index1-1] : nums2[index2-1];
        }
        if (!isEven) {
            // 奇数直接返回
            return midValue;
        } else {
            // 偶数还要取一位
            int nextValue;
            if (index1 == nums1.length) {
                nextValue = nums2[index2];
            } else if (index2 == nums2.length) {
                nextValue = nums1[index1];
            } else {
                nextValue = nums1[index1] <= nums2[index2] ? nums1[index1] : nums2[index2];
            }
            return (midValue + nextValue) /2.0D;
        }
    }

    public static void main(String[] args) {
        System.out.println(findMedianSortedArrays(new int[]{1,3}, new int[]{2}));
        System.out.println(findMedianSortedArrays(new int[]{1,2}, new int[]{3,4}));
        System.out.println(findMedianSortedArrays(new int[]{}, new int[]{1}));
    }
}
