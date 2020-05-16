package cn.jzteam.algorithm.leetcode.question0137;

/** 只出现一次的数字
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现了三次。找出那个只出现了一次的元素。
 *
 * 说明：
 *
 * 你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？
 *
 * 示例 1:
 *
 * 输入: [2,2,3,2]
 * 输出: 3
 * 示例 2:
 *
 * 输入: [0,1,0,1,0,1,99]
 * 输出: 99
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/single-number-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 题解：使用哈希比较容易的找到，但是O(1)空间的算法还是很难的
    // 异或处理，会过滤出现偶数次的数字，留下的就是出现奇数次的数字

    // 思路：int 32位，但看某一个位置，那么所有数字该位相加，这个位的结果对3取余，结果就是只出现一次的数字的那个位的值。
    // 因为重复3次的数字，该位相加结果对3取余必定是0。
    // 其实重复数字取余的结果只能 0->1->2->0 如此变化。
    // 一个位无法记录，就用两个位
    // 00 -> 01 -> 10 -> 00 （左边tiwce 右边once）
    // 如果 +0 则状态不变
    // 如果 +1 则变化一次
    // 推演状态变化，最终可以简化成位运算
    // once = ~tiwce & (once ^ n)
    // tiwce = ~once & (tiwce ^ n)
    // 观察发现 once 就是出现一次的那个位
    public static int singleNumber(int[] nums) {
        int once = 0;
        int tiwce = 0;
        for (int i=0;i<nums.length;i++) {
            once = ~tiwce & (once ^ nums[i]);
            tiwce = ~once & (tiwce ^ nums[i]);
        }
        return once;
    }

    public static void main(String[] args) {
        System.out.println(singleNumber(new int[]{ 2, 2, 2, 3, 4, 4, 4}));
    }
}
