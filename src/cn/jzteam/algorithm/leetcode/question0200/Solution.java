package cn.jzteam.algorithm.leetcode.question0200;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 *
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 *
 * 此外，你可以假设该网格的四条边均被水包围。
 *
 * 示例 1:
 *
 * 输入:
 * 11110
 * 11010
 * 11000
 * 00000
 * 输出: 1
 * 示例 2:
 *
 * 输入:
 * 11000
 * 11000
 * 00100
 * 00011
 * 输出: 3
 * 解释: 每座岛屿只能由水平和/或竖直方向上相邻的陆地连接而成。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-islands
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // 广度优先：
    // 从遇到的第一个1开始，进行一次广度优先搜索，碰到的1都改成0。当本次搜索完毕，表示这个小岛已经发现了
    public static int numIslands(char[][] grid) {
        int m=grid.length;
        if (m==0){
            return 0;
        }
        int n=grid[0].length;
        int num=0;
        Queue<int[]> points = new LinkedBlockingQueue<>();
        for (int i = 0; i<m; i++) {
            for (int j = 0;j<n;j++) {
                if (grid[i][j] == '0') {
                    continue;
                }
                num++;
                points.add(new int[]{i, j});
                while (points.size() > 0) {
                    int[] point = points.poll();
                    int x = point[0];
                    int y = point[1];
                    // 四周为1的放入队列
                    if (x-1>=0 && grid[x-1][y] == '1') {
                        grid[x-1][y] = '0';
                        points.add(new int[]{x-1, y});
                    }
                    if (x+1<m && grid[x+1][y] == '1') {
                        grid[x+1][y] = '0';
                        points.add(new int[]{x+1, y});
                    }
                    if (y-1>=0 && grid[x][y-1] == '1') {
                        grid[x][y-1] = '0';
                        points.add(new int[]{x, y-1});
                    }
                    if (y+1<n && grid[x][y+1] == '1') {
                        grid[x][y+1] = '0';
                        points.add(new int[]{x, y+1});
                    }
                }
            }
        }
        return num;
    }

    // 深度优先：
    // 从遇到的第一个1开始，进行一次深度优先搜索，碰到的1都改成0。当本次搜索完毕，表示这个小岛已经发现了
    // 深度优先使用递归，递归方法中包含所有相邻元素的判断，只有符合条件才调用递归。
    public static int numIslands1(char[][] grid) {
        if(grid.length == 0) {
            return 0;
        }
        int num=0;
        for (int i = 0; i<grid.length; i++) {
            for (int j = 0;j<grid[0].length;j++) {
                if (grid[i][j] == '0') {
                    continue;
                }
                num++;
                dg(grid, i, j);
            }
        }
        return num;
    }
    private static void dg(char[][] grid, int x, int y) {
        grid[x][y] = '0';
        if (x-1>=0 && grid[x-1][y] == '1') {
            dg(grid, x-1, y);
        }
        if (x+1<grid.length && grid[x+1][y] == '1') {
            dg(grid, x+1, y);
        }
        if (y-1>=0 && grid[x][y-1] == '1') {
            dg(grid, x, y-1);
        }
        if (y+1<grid[0].length && grid[x][y+1] == '1') {
            dg(grid, x, y+1);
        }
    }

    public static void main(String[] args) {
        System.out.println(numIslands1(new char[][]{{'1','1','1','1','0'}, {'1','1','0','1','0'}, {'1','1','0','0','0'}, {'0','0','0','0','0'}}));
        System.out.println(numIslands1(new char[][]{{'1','1','0','0','0'}, {'1','1','0','0','0'}, {'0','0','1','0','0'}, {'0','0','0','1','1'}}) );
    }
}
