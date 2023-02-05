package cn.jzteam.algorithm.leetcode.interview;

/**
 * 硬币。给定数量不限的硬币，币值为25分、10分、5分和1分，编写代码计算n分有几种表示法。(结果可能会很大，你需要将结果模上1000000007)
 *
 * 示例1:
 *
 *  输入: n = 5
 *  输出：2
 *  解释: 有两种方式可以凑成总金额:
 * 5=5
 * 5=1+1+1+1+1
 * 示例2:
 *
 *  输入: n = 10
 *  输出：4
 *  解释: 有四种方式可以凑成总金额:
 * 10=10
 * 10=5+5
 * 10=5+1+1+1+1+1
 * 10=1+1+1+1+1+1+1+1+1+1
 * 说明：
 *
 * 注意:
 *
 * 你可以假设：
 *
 * 0 <= n (总金额) <= 1000000
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/coin-lcci
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Question0811 {
    // 动态规划：优化，使用一维数组
    // 每轮新加一个币种，尝试使用新币中0~k个（k=v/面值ci），余下的金额还是使用未加新币种时的方案（方案数量已知）
    // 多项式优化之后如下：
    // f(i, v) = f(i-1, v) + f(i, v-ci)
    public static int waysToChange(int n) {
        // 币种面值
        int[] c = {1,5,10,25};
        // 一维数组
        int[] f = new int[n+1];

        // 起点边界i：f(0, *) = 1
        // 起点边界v：f(i, v<ci) = f(i-1, v)，当v=ci时，表示k=1，可以全部使用ci币种来实现一种情况，而不用i-1任何币种，所以 f(i, 0)=1;
        // 初始化：后面的遍历从i=1开始
//        for (int j=0;j<=n;j++) {
//            f[j] = 1;
//        }
        // 初始化：优化，从 f[j] = f[j] + f[j - coin] 能看出来 f[j]等于 当前值 + 旧值
        // 第一轮f[j]都是1，可以设置f[0]=1，然后上面公式可以保证第一轮f[j]都是1。当时如果c[0]=5，就需要初始化f[0]到f[4]都是1了。
        f[0] = 1;
        for (int i=0;i<c.length;i++) {
            int coin = c[i];
            for (int j = coin; j <= n; j++) {
                // 新值 f(v) = 旧值 f(v) + 新值f(v-ci)
                // 一维数组以j为边界，靠右都是上一次设置的值f(i-1, v)，即旧值，靠左都是刚刚设置过的f(i, v)，即新值
                // j 从 ci面值 开始，刚开始依赖 f(i, v-ci)，即f(i, 0~ci)，这些新值根本没有设置，所以还是上一次的旧值，即f(i-1, 0-ci)
                // 然而，当 v < ci 时，f(i, v) = f(i-1, v)，所以直接使用未设置新值的f(i, 0-ci)满足条件
                f[j] = (f[j] + f[j - coin]) % 1000000007;
            }
        }
        return f[n];
    }

    // 动态规划
    // 每轮新加一个币种，尝试使用新币中1~k个（k=v/面值ci），余下的金额还是使用未加新币种时的方案（方案数量已知）
    // 多项式优化之后如下：
    // f(i, v) = f(i-1, v) + f(i, v-ci)
    public static int waysToChange1(int n) {
        // 币种面值
        int[] c = {1,5,10,25};
        // 二维数组
        int[][] f = new int[2][n+1];
        // 起点边界i：f(0, *) = 1
        // 起点边界v：f(i, v<ci) = f(i-1, v)，当v=ci时，表示k=1，可以全部使用ci币种来实现一种情况，而不用i-1任何币种，所以 f(i, 0)=1;
        // 初始化：
        for (int j=0;j<=n;j++) {
            f[0][j] = 1;
        }
        for (int i =1;i<c.length;i++) {
            int index = i%2;
            for (int j=0;j<=n;j++) {
                // j < ci面值 时，f[i][j] = f[i-1][j]，所以令f[i, j-ci] = 0
                // 这里j不能以c[i]为起点，是因为f[i][c[i]] 依赖 f[i][0]了，而f[i][0]会因为没有设置为0而可能取到上一次的值1
                int temp = j < c[i] ? 0 : f[index][j-c[i]];
                f[index][j] = (f[Math.abs(index-1)][j] + temp) % 1000000007;
            }
        }
        return f[1][n];
    }

    public static void main(String[] args) {
        // System.out.println(waysToChange(0));
        System.out.println(waysToChange(5));
        System.out.println(waysToChange(10));
        System.out.println(waysToChange(900750)); // 504188296
    }
}
