package cn.jzteam.algorithm.leetcode.question0983;

/** 最低票价
 * 在一个火车旅行很受欢迎的国度，你提前一年计划了一些火车旅行。在接下来的一年里，你要旅行的日子将以一个名为 days 的数组给出。每一项是一个从 1 到 365 的整数。
 *
 * 火车票有三种不同的销售方式：
 *
 * 一张为期一天的通行证售价为 costs[0] 美元；
 * 一张为期七天的通行证售价为 costs[1] 美元；
 * 一张为期三十天的通行证售价为 costs[2] 美元。
 * 通行证允许数天无限制的旅行。 例如，如果我们在第 2 天获得一张为期 7 天的通行证，那么我们可以连着旅行 7 天：第 2 天、第 3 天、第 4 天、第 5 天、第 6 天、第 7 天和第 8 天。
 *
 * 返回你想要完成在给定的列表 days 中列出的每一天的旅行所需要的最低消费。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：days = [1,4,6,7,8,20], costs = [2,7,15]
 * 输出：11
 * 解释：
 * 例如，这里有一种购买通行证的方法，可以让你完成你的旅行计划：
 * 在第 1 天，你花了 costs[0] = $2 买了一张为期 1 天的通行证，它将在第 1 天生效。
 * 在第 3 天，你花了 costs[1] = $7 买了一张为期 7 天的通行证，它将在第 3, 4, ..., 9 天生效。
 * 在第 20 天，你花了 costs[0] = $2 买了一张为期 1 天的通行证，它将在第 20 天生效。
 * 你总共花了 $11，并完成了你计划的每一天旅行。
 * 示例 2：
 *
 * 输入：days = [1,2,3,4,5,6,7,8,9,10,30,31], costs = [2,7,15]
 * 输出：17
 * 解释：
 * 例如，这里有一种购买通行证的方法，可以让你完成你的旅行计划：
 * 在第 1 天，你花了 costs[2] = $15 买了一张为期 30 天的通行证，它将在第 1, 2, ..., 30 天生效。
 * 在第 31 天，你花了 costs[0] = $2 买了一张为期 1 天的通行证，它将在第 31 天生效。
 * 你总共花了 $17，并完成了你计划的每一天旅行。
 *  
 *
 * 提示：
 *
 * 1 <= days.length <= 365
 * 1 <= days[i] <= 365
 * days 按顺序严格递增
 * costs.length == 3
 * 1 <= costs[i] <= 1000
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/minimum-cost-for-tickets
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 题解：动态规划
    // 因为通行证的有效期是向后延伸，所以动态规划需要倒着进行
    // dp(i) = min{cost(j) + dp(i+j)}, j 取值 {1, 7, 30}
    // dp(i) 表示一年中任意一天到年底，出行花费最小值
    // 如果i不是出行日期，不用买票，dp(i) = dp(i+1)
    // 如果i是出行日期，则可以买 1，7，30天通行证，而且在接下来的 j-1 天内都不用买票，即只需要考虑 i+j 天及以后的最小花费
    public static int mincostTickets(int[] days, int[] costs) {
        if (days.length == 0 || costs.length == 0) {
            return 0;
        }

        int[] dp = new int[366];

        int dayPos = days.length;
        int day = days[--dayPos];

        for (int i=365;i>=1;i--){
            if (i == day) {
                // 是出行日期
                // 买1天
                int cost1 = costs[0] + (i+1 > 365 ? 0 : dp[i+1]);
                // 买7天
                int cost7 = costs[1] + (i+7 > 365 ? 0 : dp[i+7]);
                // 买30天
                int cost30 = costs[2] + (i+30 > 365 ? 0 : dp[i+30]);
                // 取最小值
                dp[i] = Math.min(Math.min(cost1, cost7), cost30);
                // 下一个出行日期
                if (dayPos == 0) {
                    return dp[i];
                } else {
                    day = days[--dayPos];
                }
            } else {
                dp[i] = i+1 > 365 ? 0 : dp[i+1];
            }
        }
        return dp[1];
    }

    public static void main(String[] args) {
        System.out.println(mincostTickets(new int[]{1,4,6,7,8,20}, new int[]{2, 7, 15})); // 11
        System.out.println(mincostTickets(new int[]{1,2,3,4,5,6,7,8,9,10,30,31}, new int[]{2, 7, 15})); // 17
        System.out.println(mincostTickets(new int[]{}, new int[]{2, 7, 15})); //  0
    }
}
