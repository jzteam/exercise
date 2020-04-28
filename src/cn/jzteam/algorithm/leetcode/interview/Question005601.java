package cn.jzteam.algorithm.leetcode.interview;

import com.alibaba.fastjson.JSON;

/**
 * 一个整型数组 nums 里除两个数字之外，其他数字都出现了两次。请写程序找出这两个只出现一次的数字。要求时间复杂度是O(n)，空间复杂度是O(1)。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：nums = [4,1,4,6]
 * 输出：[1,6] 或 [6,1]
 * 示例 2：
 *
 * 输入：nums = [1,2,10,4,1,4,3,3]
 * 输出：[2,10] 或 [10,2]
 *  
 *
 * 限制：
 *
 * 2 <= nums <= 10000
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Question005601 {
    // 分组异或
    // 相同得0，不同得1，多个数字异或结果，跟那两个独立的数字异或结果是一样的
    // 先计算出来这个异或结果，找到其中一个不同的位
    // 根据这个位，就能将nums分成两组，相同的数组这个位肯定相同，所以都在一组，跟这个位不同的都在另一组
    // 其实这两组的特点就是，各包含一个独立数字，其他数组都重复。
    // 核心就是根据一个特点区分出两类
    public static int[] singleNumbers(int[] nums) {
        // 计算异或结果
        int result = 0;
        for (int i=0;i<nums.length;i++) {
            result ^= nums[i];
        }
        // 找一个不同的位
        int pos = 0;
        while ((result >>> pos++) % 2 != 1);
        pos--;

        int num1 = 0;
        int num2 = 0;
        // 根据这个位来分组
        for (int i=0;i<nums.length;i++) {
            // >>> 优先级 比 % 低，要加括号
            if ((nums[i] >>> pos) % 2 == 1) {
                num1 ^= nums[i];
            } else {
                num2 ^= nums[i];
            }
        }
        return new int[]{num1, num2};
    }

    public static void main(String[] args) {
//        System.out.println(JSON.toJSONString(singleNumbers(new int[]{4,1,4,6})));
//        System.out.println(JSON.toJSONString(singleNumbers(new int[]{1,2,10,4,1,4,3,3})));
        System.out.println(JSON.toJSONString(singleNumbers(new int[]{6,2,3,3}))); // 6,2
    }
}
