package cn.jzteam.algorithm.leetcode.interview;

/**
 * 写一个函数，输入 n ，求斐波那契（Fibonacci）数列的第 n 项。斐波那契数列的定义如下：
 *
 * F(0) = 0,   F(1) = 1
 * F(N) = F(N - 1) + F(N - 2), 其中 N > 1.
 * 斐波那契数列由 0 和 1 开始，之后的斐波那契数就是由之前的两数相加而得出。
 *
 * 答案需要取模 1e9+7（1000000007），如计算初始结果为：1000000008，请返回 1。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：n = 2
 * 输出：1
 * 示例 2：
 *
 * 输入：n = 5
 * 输出：5
 *  
 *
 * 提示：
 *
 * 0 <= n <= 100
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/fei-bo-na-qi-shu-lie-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Question0010 {
    // 方案一：递归，无限耗时
    public static int fib(int n) {
        return fib_dg(n);
    }

    private static int fib_dg (int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib_dg(n-1) + fib_dg(n-2);
    }

    // 方案二：动态规划
    public static int fib1(int n) {
        int[] temp = new int[3];
        temp[0] = 0;
        temp[1] = 1;
        if (n >= 2) {
            for (int i=2;i<=n;i++) {
                int index = i % 3;
                temp[index] = (temp[(index+1)%3] + temp[(index+2)%3]) % 1000000007;
            }
        }
        return temp[n%3];
    }

    public static void main(String[] args) {
        System.out.println(fib(2));
        System.out.println(fib(5));
        System.out.println(fib(7)); // 13
        System.out.println(fib(48)); // 807526948
    }
}
