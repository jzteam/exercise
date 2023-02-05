package cn.jzteam.algorithm.leetcode.question0887;

public class Solution {
    public static int superEggDrop(int K, int N) {
        // 设 K 个鸡蛋，T 次移动，能测试的楼层高度最大为 f(K, T)
        // 扔一个鸡蛋之后，碎了则还能往下测试的最大高度为 f(K-1, T-1)
        // 没有碎则还能往上测试的最大高度为 f(K, T-1)
        // f(K, T) = 1 + f(K-1, T-1) + f(K, T-1)
        // 边界条件：f(1, T) = T，f(K, 1) = 1，T<=N
        // N = f(K, T) 的最大值，可以暴力尝试所有T值（1<=T<=N）
        int[][] f = new int[K+1][N+1];
        int min = N;
        // 先把边界条件设置好
        for (int k = 1; k <= K; k++) {
            f[k][1] = 1;
        }
        // t表示次数，最少次数已经满足 >=N，即可直接返回了
        for (int t = 2; t <= N; t++) {
            for (int k = 1; k <= K; k++) {
                if (k == 1) {
                    f[k][t] = t;
                } else {
                    f[k][t] = 1 + f[k - 1][t - 1] + f[k][t - 1];
                }
            }
            // k表示鸡蛋个数，k<=K的所有可能都已经尝试过了，直接使用K来取值即可（次数固定，鸡蛋再多也不能增加高度了）
            if (f[K][t] >= N && t < min) {
                min = t;
                break;
            }
        }
        return min;
    }

    public static void main(String[] args) {
        System.out.println(superEggDrop(1, 2));
        System.out.println(superEggDrop(2, 6));
        System.out.println(superEggDrop(3, 14));
        System.out.println(superEggDrop(1, 3));
        System.out.println(superEggDrop(3, 1));
        System.out.println(superEggDrop(9, 2));
        System.out.println(superEggDrop(9, 3));
        System.out.println(superEggDrop(9, 4));
        System.out.println(superEggDrop(9, 6));
    }
}
