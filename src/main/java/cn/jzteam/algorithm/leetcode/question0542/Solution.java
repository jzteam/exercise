package cn.jzteam.algorithm.leetcode.question0542;

import com.alibaba.fastjson.JSON;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/*
    给定一个由 0 和 1 组成的矩阵，找出每个元素到最近的 0 的距离。

    两个相邻元素间的距离为 1 。

    示例 1:
    输入:

    0 0 0
    0 1 0
    0 0 0
    输出:

    0 0 0
    0 1 0
    0 0 0
    示例 2:
    输入:

    0 0 0
    0 1 0
    1 1 1
    输出:

    0 0 0
    0 1 0
    1 2 1
    注意:

    给定矩阵的元素个数不超过 10000。
    给定矩阵中至少有一个元素是 0。
    矩阵中的元素只在四个方向上相邻: 上、下、左、右。

    来源：力扣（LeetCode）
    链接：https://leetcode-cn.com/problems/01-matrix
    著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 广度优先：从0主动扩散找1
    // 这个扩散有两个核心点：
    // 1、每一次扩散，只找相邻的4个点（上下左右）未设置值的点，设置距离为当前点距离+1，然后该点就是下轮扩散的起点
    // 2、每一轮扩散，都是所有起点同时开始扩展一次
    // 这个规则解决了我的两个疑惑：
    // 1、两个0中间的那个1的距离怎么确定？ 因为每次扩散都是距离+1，距离最近的0肯定是最先找到这个1，设置了值之后就不再变了。
    // 2、从0扩散，只想到纵横，可能找不到1？ 因为每次扩散只走1步，而且是上下左右，所以能够全方位覆盖
    public static int[][] updateMatrix(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        // 标记是否设置过值
        int[][] flags = new int[m][n];
        int[][] result = new int[m][n];
        Queue<int[]> points = new LinkedBlockingQueue<>();

        // 四个方向
        int[][] dist = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        // 找到第一轮的起点
        for(int i=0;i<m;i++) {
            for (int j=0;j<n;j++) {
                if (matrix[i][j] == 0) {
                    flags[i][j] = 1;
                    // 第一轮的起点
                    points.add(new int[]{i, j});
                }
            }
        }
        // O(mn)
        while (!points.isEmpty()) {
            int[] point = points.poll();
            int x = point[0];
            int y = point[1];
            // 遍历四个方向的相邻点
            for (int i=0;i<dist.length;i++) {
                // 相邻点的坐标
                int nx = x + dist[i][0];
                int ny = y + dist[i][1];
                // 没越界，且没设置过值，才设值
                if (nx >= 0 && nx < m && ny >= 0 && ny < n && flags[nx][ny] != 1) {
                    result[nx][ny] = result[x][y] + 1;
                    flags[nx][ny] = 1;
                    points.add(new int[]{nx, ny});
                }
            }
        }
        return result;
    }


    // 动态规划：从1主动去找0
    // 任何为1的点到最近的0的距离为f(x,y)，那么可以概括成四种路径，就是
    // 只向左 + 只向上
    // 只向左 + 只向下
    // 只向右 + 只向上
    // 只向右 + 只向下
    // 为什么概括成这四种呢？因为动态规划是依赖于状态变化的，而状态变化是线性的，所以这里概括的路径也要是线性的
    // 举例（只向左 + 只向上）：
    // f(x,y) = 1 + min(f(x-1,j), f(x, j-1))
    // 边界条件是 当前点的值为0，则f(x,y)=0

    public static int[][] updateMatrix1(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int max = m + n;
        int[][] f = new int[m][n];
        // 初始化
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    f[i][j] = 0;
                } else {
                    f[i][j] = max;
                }
            }
        }
        // 只向左 + 只向上
        // 因为向左是 i-1，所以i要从最小的开始，后面的i才能依赖i-1
        for (int i = 0; i < m; i++) {
            // 因为向上是 j-1，所以j要从最小的开始，后面的j才能依赖j-1
            for (int j = 0; j < n; j++) {
                // 最左边的点不能再向左了，就不横向向左寻找了，可以向上；还有其他3种路径
                if (i - 1 >= 0) {
                    // 自身是0，就用自身的值了：一路向左
                    f[i][j] = Math.min(f[i][j], f[i-1][j] + 1);
                }
                // 最上边的点不能再向上了，就不纵向向上寻找了，可以向左；还有其他3种路径
                if (j - 1 >= 0) {
                    // 自身是0，就用自身的值了：一路向上
                    f[i][j] = Math.min(f[i][j], f[i][j-1] + 1);
                }
            }
        }
        // 只向右 + 只向下
        // 因为向右是 i+1，所以i要从最大的开始，后面的i才能依赖i+1
        for (int i = m-1; i >= 0; i--) {
            // 因为向下是 j+1，所以j要从最大的开始，后面的j才能依赖j+1
            for (int j = n-1; j >= 0; j--) {
                // 最右边的点不能再向右了，就不横向向右寻找了，可以向下；还有其他3种路径
                if (i + 1 < m) {
                    // 自身是0，就用自身的值了：一路向左
                    f[i][j] = Math.min(f[i][j], f[i+1][j] + 1);
                }
                // 最下边的点不能再向下了，就不纵向向下寻找了，可以向左；还有其他3种路径
                if (j + 1 < n) {
                    // 自身是0，就用自身的值了：一路向下
                    f[i][j] = Math.min(f[i][j], f[i][j+1] + 1);
                }
            }
        }
        // （只向左 + 只向下） 和 （只向右 + 只向上）跟上面两种重复了
        // 相当于在每一个点上用激光扫描，发现最近的点，就标记上距离，但是扫描方向只有一半，不是全方位
        // 那么在相反的方向再扫描一遍，就相当于全方位扫描了
        return f;
    }

    // 个人方案，优化 速度 5%，内存100%
    // 找到所有为0的坐标，找到所有1的坐标
    // 两个坐标能计算出距离，2层循环找出最短距离
    public static int[][] updateMatrix2(int[][] matrix) {
        int[][] data = new int[matrix.length][matrix[0].length];
        // 为0元素的坐标
        int[][] point0 = new int[matrix.length * matrix[0].length][2];
        int pos0 = 0;

        // 为1元素的坐标
        int[][] point1 = new int[matrix.length * matrix[0].length][2];
        int pos1 = 0;

        for (int i=0;i<matrix.length;i++) {
            for (int j=0;j<matrix[0].length;j++) {
                if (matrix[i][j] == 0) {
                    point0[pos0][0] = i;
                    point0[pos0][1] = j;
                    pos0++;
                } else {
                    point1[pos1][0] = i;
                    point1[pos1][1] = j;
                    pos1++;
                    data[i][j] = 100000;
                }
            }
        }
        for (int i = 0; i < pos1; i++) {
            // 获取所有1的坐标
            int x1 = point1[i][0];
            int y1 = point1[i][1];
            for (int j = 0; j < pos0; j++) {
                // 遍历所有0的坐标，找到距离1最近的那个
                int x0 = point0[j][0];
                int y0 = point0[j][1];
                int d = Math.abs(x1-x0) + Math.abs(y1-y0);
                if (d == 1) {
                    data[x1][y1] = d;
                    break;
                }
                if (d < data[x1][y1]) {
                    data[x1][y1] = d;
                }
            }
        }
        return data;
    }

    public static void main(String[] args) {
        int[][] ints = {{0, 0, 0}, {0, 1, 0}, {1, 1, 1}};
        System.out.println(JSON.toJSONString(updateMatrix(ints)));

        int[][] tt = {{1,0,1,1,0,0,1,0,0,1},{0,1,1,0,1,0,1,0,1,1},{0,0,1,0,1,0,0,1,0,0},{1,0,1,0,1,1,1,1,1,1},{0,1,0,1,1,0,0,0,0,1},{0,0,1,0,1,1,1,0,1,0},{0,1,0,1,0,1,0,0,1,1},{1,0,0,0,1,1,1,1,0,1},{1,1,1,1,1,1,1,0,1,0},{1,1,1,1,0,1,0,0,1,1}};
        System.out.println(JSON.toJSONString(updateMatrix(tt)));
    }
}
