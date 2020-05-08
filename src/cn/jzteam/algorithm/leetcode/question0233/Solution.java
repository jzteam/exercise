package cn.jzteam.algorithm.leetcode.question0233;

/** 数字1的个数
 * 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
 *
 * 示例:
 *
 * 输入: 13
 * 输出: 6
 * 解释: 数字 1 出现在以下数字中: 1, 10, 11, 12, 13 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-digit-one
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // 个人方案：排列组合
    // 屏蔽最高位，1出现的次数：count = 累加器1~(w-1) [i*Ci_w-1*9^(w-1-i)]，w表示位数
    // 包括最高位：
    //      最高位取1的情况：累加器0~(w-1) [(i+1)*Ci_w-1*9^(w-1-i)]，w表示位数
    //      最高位取最大值的情况：递归(去掉最高位后的数字)
    //      最高位其他可能：除掉1和最大值两种情况剩下的可能数量 * count
    public static int countDigitOne(int n) {
        return count(n);
    }

    public static int count(int n) {
        if (n < 0) {
            return 0;
        }
        // 计算位数
        int w = (n+"").length();
        if (w == 1) {
            return n > 0 ? 1 : 0;
        }
        int sum = 0;
        // 计算屏蔽最高位的情况
        for (int i=1;i<=w-1;i++) {
            sum += i * c(i, w-1) * Math.pow(9, w-1-i);
        }

        // 计算最高位不取最大值，且不取1的情况
        int gn = n / (int)Math.pow(10, w-1);
        if (gn > 2) {
            sum += (gn-2) * sum; // 此情况依赖 屏蔽最高位的总数，所以放前面，直接使用sum变量（此时sum还没包括其他情况的数据）
        }
        // 计算最高位取最大值
        if (gn > 1) {
            // 最高位不是1，但是取值为1的情况
            for (int i=0;i<=w-1;i++) {
                sum += (i+1) * c(i, w-1) * Math.pow(9, w-1-i);
            }
            // 最高位不是1，取最大值的情况
            sum += count(n % (int)Math.pow(10, w-1));
        } else {
            // 最高位就是1，会比最高位不是1的情况多出 rem+1个1，+1是因为取余的到的数字不包括0这种情况
            int rem = n % (int) Math.pow(10, w - 1);
            sum += count(rem);
            sum += rem+1;
        }
        return sum;
    }

    public static int c (int selected, int total) {
        int num = 1;
        while (total > selected) {
            num *= total--;
        }
        return num;
    }

    public static void main(String[] args) {
        // System.out.println(countDigitOne(10));
        System.out.println(countDigitOne(999)); // 300
    }
}
