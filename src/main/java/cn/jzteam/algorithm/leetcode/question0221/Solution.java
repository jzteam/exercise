package cn.jzteam.algorithm.leetcode.question0221;

import java.util.ArrayList;
import java.util.List;

/**
 * 在一个由 0 和 1 组成的二维矩阵内，找到只包含 1 的最大正方形，并返回其面积。
 *
 * 示例:
 *
 * 输入:
 *
 * 1 0 1 0 0
 * 1 0 1 1 1
 * 1 1 1 1 1
 * 1 0 0 1 0
 *
 * 输出: 4
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximal-square
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 题解：动态规划
    // 使用 dp(i, j) 表示以i,j 为右下角的正方形的最大边长
    // 那么 dp(i, j) = min(dp(i-1, j), dp(i-1, j-1), dp(i, j-1)) + 1
    // 即左、上、左上三个位置的最小值 +1。
    // 可以这么理解，i,j为右下角的正方形的面积，需要 左、上，左上 三个方向同时发力，木桶效应，发力最小的那个决定了以i,j为右下角的正方形的最大面积（边长）
    public static int maximalSquare(char[][] matrix) {
        // 最大边长
        int max = 0;
        int m = matrix.length;
        if (m == 0) {
            return 0;
        }
        int n = matrix[0].length;
        int[][] data = new int[m][n];

        // 第一列和第一行可以直接初始化，减少后面循环中的越界判断
        for (int i=0;i<m;i++) {
            if (matrix[i][0] == '1') {
                data[i][0] = 1;
                if (max == 0) {
                    max = 1;
                }
            }
        }
        for (int i=1;i<n;i++) {
            if (matrix[0][i] == '1') {
                data[0][i] = 1;
                if (max == 0) {
                    max = 1;
                }
            }
        }
        // dp(i, j) = min(dp(i-1, j), dp(i-1, j-1), dp(i, j-1)) + 1
        for (int i=1;i<m;i++) {
            for (int j=1;j<n;j++) {
                if (matrix[i][j] == '1') {
                    data[i][j] = Math.min(Math.min(data[i-1][j], data[i][j-1]), data[i-1][j-1]) + 1;
                    if (data[i][j] > max) {
                        max = data[i][j];
                    }
                }
            }
        }
        return max*max;
    }
    // 个人方案：以每一个1为正方形的左顶点，计算最大正方形面积。然后取最大值
    public static int maximalSquare1(char[][] matrix) {
        List<int[]> points = new ArrayList<>();
        int m = matrix.length;
        if (m == 0) {
            return 0;
        }
        int n = matrix[0].length;

        // 寻找每一个1，都有可能是左顶点
        for (int i=0;i<m;i++) {
            for (int j=0;j<n;j++) {
                if (matrix[i][j] == '1') {
                    points.add(new int[]{i, j});
                }
            }
        }

        if (points.size() == 0) {
            return 0;
        }

        // 寻找能够扩张最多的1
        int maxExtend = 0;
        for (int[] point : points) {
            int temp=1;
            label1:
            while (true) {
                int x_end = point[0] + temp;
                int y_end = point[1] + temp;

                if (x_end >= m || y_end >= n) {
                    // 超出边界，不用扩张了
                    break label1;
                }
                // 判断外围全是1，则继续扩张，否则不用扩张了
                for (int i=point[0];i<=x_end;i++) {
                    if (matrix[i][y_end] != '1') {
                        break label1;
                    }
                }
                for (int j=point[1];j<y_end;j++) {
                    if (matrix[x_end][j] != '1') {
                        break label1;
                    }
                }
                // 继续扩张
                temp++;
            }
            if (--temp > maxExtend) {
                maxExtend = temp;
            }
        }

        return (1+maxExtend)*(1+maxExtend);
    }

    public static void main(String[] args) {
        /*
            1 0 1 0 0
            1 0 1 1 1
            1 1 1 1 1
            1 0 0 1 0
         */
        char[][] data = new char[][]{
                {'1','0','1','0','0'},
                {'1','0','1','1','1'},
                {'1','1','1','1','1'},
                {'1','0','0','1','0'}};
        System.out.println(maximalSquare(data));
        System.out.println(maximalSquare(new char[][]{{'1', '0'}}));
    }

}
