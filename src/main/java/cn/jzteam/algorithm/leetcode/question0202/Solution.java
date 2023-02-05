package cn.jzteam.algorithm.leetcode.question0202;

/**
 * 编写一个算法来判断一个数 n 是不是快乐数。
 *
 * 「快乐数」定义为：对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和，然后重复这个过程直到这个数变为 1，也可能是 无限循环 但始终变不到 1。如果 可以变为  1，那么这个数就是快乐数。
 *
 * 如果 n 是快乐数就返回 True ；不是，则返回 False 。
 *
 *  
 *
 * 示例：
 *
 * 输入：19
 * 输出：true
 * 解释：
 * 12 + 92 = 82
 * 82 + 22 = 68
 * 62 + 82 = 100
 * 12 + 02 + 02 = 1
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/happy-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // 题解：
//    初步判断，只有以下三种可能。
//    最终会得到 1。
//    最终会进入循环。
//    值会越来越大，最后接近无穷大。
    // 然而仔细推算，第三种情况不可能。比如3位数的一个数组，无论怎么折腾，达到最大值不过是999，而999的各位平方和是243，又折回去了。
    // 所有数字都是如此，不可能越来越大，所以最终结果要么是循环，要么是1。（1也是一种循环）
    public static boolean isHappy(int n) {
        // 快慢指针
        int faster = getNext(n);
        int slower = n;
        // 慢指针每次走一步，快指针每次走两步。快指针一定先到1或者发现循环
        while (faster != 1 && faster != slower) {
            slower = getNext(slower);
            faster = getNext(getNext(faster));
        }
        return faster == 1;
    }

    // 快慢指针，获取平方和
    private static int getNext(int n) {
        int result = 0;
        while (n > 0) {
            int num = n % 10;
            n = n / 10;
            result += num*num;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(isHappy(19));
    }
}
