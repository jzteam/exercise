package cn.jzteam.algorithm.leetcode.question0069;

/**
 * 实现 int sqrt(int x) 函数。
 *
 * 计算并返回 x 的平方根，其中 x 是非负整数。
 *
 * 由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
 *
 * 示例 1:
 *
 * 输入: 4
 * 输出: 2
 * 示例 2:
 *
 * 输入: 8
 * 输出: 2
 * 说明: 8 的平方根是 2.82842...,
 *      由于返回类型是整数，小数部分将被舍去。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/sqrtx
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // 题解：牛顿迭代法
    // y=x*x - C 无线趋近于0时，x就是C的平方根了。
    // 从 x=C开始，做y曲线的切线f，f相较x轴于x0，如果x0=0，就表示得到解了。
    // 时间复杂度 O(logC)，二次收敛，比二分法还快一些
    // 设增量t，yt=(x+t)*(x+t)-C，y0=x*x-C, 斜率k=(yt-y0)/t = 2x+t。t无限趋近于0时，k=2x
    // 则对于任一点m，此处的斜线方程为：y=2m(x-m)+m*m-C
    // 与x轴交点为 (m*m+C)/(2*m)，当其无限趋近于m时，m就是解。
    public static int mySqrt(int x) {
        if (x == 0){
            return 0;
        }
        double m = x;
        while (true) {
            double x0 = 0.5*(m+x/m);
            if (m-x0<0.00001) {
                break;
            } else {
                m = x0;
            }
        }
        return (int)m;
    }

    // 题解：二分查找
    // 从 0-x中寻找一个数m，m*m=x。因为x是整数，所以m>=1，其平方是递增函数。
    public static int mySqrt1(int x) {
        // 特殊值处理
        if (x == 0) return 0;
        if (x == 1) return 1;

        double start = 0;
        double end = x;
        double m = (start + end) / 2;
        while (true) {
            double temp = m*m;
            if (Math.abs(temp - x) < 0.00001) {
                return (int)m;
            }
            // 不合格则范围重新界定
            if (temp > x) end = m;
            if (temp < x) start = m;
            // m 取范围二分值
            m = (start + end) / 2;
        }
    }

    public static void main(String[] args) {
        System.out.println(mySqrt1(4));
        System.out.println(mySqrt1(9));
        System.out.println(mySqrt1(8));
    }
}
