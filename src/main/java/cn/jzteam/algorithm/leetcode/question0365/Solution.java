package cn.jzteam.algorithm.leetcode.question0365;

import cn.jzteam.utils.MyMath;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 有两个容量分别为 x升 和 y升 的水壶以及无限多的水。请判断能否通过使用这两个水壶，从而可以得到恰好 z升 的水？
 *
 * 如果可以，最后请用以上水壶中的一或两个来盛放取得的 z升 水。
 *
 * 你允许：
 *
 * 装满任意一个水壶
 * 清空任意一个水壶
 * 从一个水壶向另外一个水壶倒水，直到装满或者倒空
 * 示例 1: (From the famous "Die Hard" example)
 *
 * 输入: x = 3, y = 5, z = 4
 * 输出: True
 * 示例 2:
 *
 * 输入: x = 2, y = 6, z = 5
 * 输出: False
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/water-and-jug-problem
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 题解：数学法
    // ax + by = z
    // ab若为负数，表示需要整壶倒掉的次数，若为正数，表示用水总量。
    // 0 < z < x + y
    // 贝祖定理告诉我们，ax+by=z 有解当且仅当 z 是 x, y的最大公约数的倍数。
    // 因此我们只需要找到 x, y的最大公约数并判断 z 是否是它的倍数即可。
    public static boolean canMeasureWater(int x, int y, int z) {
        // 范围
        if (z > x + y) {
            return false;
        }
        // 特殊范围：少了一个壶的情况，z只能是0或者另一个壶的容量才能实现
        if (x == 0 || y == 0) {
            return z == 0 || z == (x + y);
        }
        return z % MyMath.gcd(x, y) == 0;
    }

    // 深度优先探索
    // 以此刻x和y中剩余的水量为状态，下一个状态是有限的：只有6种
    // 把 X 壶的水灌进 Y 壶，直至灌满或倒空；
    // 把 Y 壶的水灌进 X 壶，直至灌满或倒空；
    // 把 X 壶灌满；
    // 把 Y 壶灌满；
    // 把 X 壶倒空；
    // 把 Y 壶倒空
    // 如果使用递归，以8的指数级增长显然不够用，所以换成使用栈
    // 为了防止无限循环，增加HashSet来去重，栈中所有状态都重复了，不会再入栈了，最终返回false
    // 任一状态满足z，则返回true
    public static boolean canMeasureWater1(int x, int y, int z) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{0, 0});
        Set<String> set = new HashSet<>();
        while (!stack.empty()) {
            int[] pop = stack.pop();
            int remain_x = pop[0];
            int remain_y = pop[1];
            if (remain_x == z || remain_y == z || remain_x + remain_y == z) {
                return true;
            }
            // 放入set表示处理过的状态
            String key = remain_x + "_" + remain_y;
            if (set.contains(key)) {
                continue;
            } else {
                set.add(key);
            }
            // 把 X 壶的水灌进 Y 壶，直至灌满或倒空；
            stack.push(new int[]{remain_x - Math.min(remain_x, y-remain_y), Math.min(y, remain_x + remain_y)});
            // 把 Y 壶的水灌进 X 壶，直至灌满或倒空；
            stack.push(new int[]{Math.min(x, remain_x + remain_y), remain_y - Math.min(remain_y, x-remain_x)});
            // 把 X 壶灌满；
            stack.push(new int[]{x, remain_y});
            // 把 Y 壶灌满；
            stack.push(new int[]{remain_x, y});
            // 把 X 壶倒空；
            stack.push(new int[]{0, remain_y});
            // 把 Y 壶倒空
            stack.push(new int[]{remain_x, 0});
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(canMeasureWater1(3,5, 4));
        System.out.println(canMeasureWater1(2,6, 5));
        System.out.println(canMeasureWater1(104707, 104711, 1));
    }
}
