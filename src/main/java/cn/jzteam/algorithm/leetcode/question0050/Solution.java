package cn.jzteam.algorithm.leetcode.question0050;

/** 50. Pow(x, n)
 * 实现 pow(x, n) ，即计算 x 的 n 次幂函数。
 *
 * 示例 1:
 *
 * 输入: 2.00000, 10
 * 输出: 1024.00000
 * 示例 2:
 *
 * 输入: 2.10000, 3
 * 输出: 9.26100
 * 示例 3:
 *
 * 输入: 2.00000, -2
 * 输出: 0.25000
 * 解释: 2-2 = 1/22 = 1/4 = 0.25
 * 说明:
 *
 * -100.0 < x < 100.0
 * n 是 32 位有符号整数，其数值范围是 [−231, 231 − 1] 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/powx-n
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 题解：快速幂迭代法
    // x的n次方，可以看作是 n个x并排，对半处理，得到一半，再平方就得到两半之积了。如果n是奇数，多乘一次x即可。
    // 这就是递归的做法，从n开始逐渐减小，每一步都能知道需要多乘一次x。不过栈空间开销较大
    // 迭代的做法难点在于无法提前知道是否需要多乘一次x。
    public static double myPow(double x, int n) {
        long N = n;
        return N>=0 ? dg(x, N) : (1.0/dg(x, -N));
    }

    // n必须为正数
    public static double dg (double x, long n) {
        if (n == 0) {
            return 1.0;
        }
        // 折半，先求之中一半的结果
        double result = dg (x, n/2);
        // 两半结果相乘
        result *= result;
        // n为奇数则需要多乘一次x
        if (n%2 == 1) {
            result *= x;
        }
        return result;
    }

    // n必须为正数
    // TODO ???
    public static double quick (double x, int n) {
        double result = 1;
        // n 表示 还需要乘以x的个数
        while (n > 0) {
            // 如果n不是偶数，则先乘一次
            if (n % 2 == 1) {
                result *= x;
            }
            // 指数折半，那么
            n = n/2;
            result *= result;
        }
        return result;
    }

    public static void main(String[] args) {
        int n = -2147483648;
        System.out.println(n>=0);
        System.out.println(0-n); // TODO 无效，还是 -2147483648
        System.out.println(myPow(2.0, n));
    }


}
