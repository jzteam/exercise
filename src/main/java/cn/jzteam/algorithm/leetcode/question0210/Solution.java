package cn.jzteam.algorithm.leetcode.question0210;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * 现在你总共有 n 门课需要选，记为 0 到 n-1。
 *
 * 在选修某些课程之前需要一些先修课程。 例如，想要学习课程 0 ，你需要先完成课程 1 ，我们用一个匹配来表示他们: [0,1]
 *
 * 给定课程总量以及它们的先决条件，返回你为了学完所有课程所安排的学习顺序。
 *
 * 可能会有多个正确的顺序，你只要返回一种就可以了。如果不可能完成所有课程，返回一个空数组。
 *
 * 示例 1:
 *
 * 输入: 2, [[1,0]]
 * 输出: [0,1]
 * 解释: 总共有 2 门课程。要学习课程 1，你需要先完成课程 0。因此，正确的课程顺序为 [0,1] 。
 * 示例 2:
 *
 * 输入: 4, [[1,0],[2,0],[3,1],[3,2]]
 * 输出: [0,1,2,3] or [0,2,1,3]
 * 解释: 总共有 4 门课程。要学习课程 3，你应该先完成课程 1 和课程 2。并且课程 1 和课程 2 都应该排在课程 0 之后。
 *      因此，一个正确的课程顺序是 [0,1,2,3] 。另一个正确的排序是 [0,2,1,3] 。
 * 说明:
 *
 * 输入的先决条件是由边缘列表表示的图形，而不是邻接矩阵。详情请参见图的表示法。
 * 你可以假定输入的先决条件中没有重复的边。
 * 提示:
 *
 * 这个问题相当于查找一个循环是否存在于有向图中。如果存在循环，则不存在拓扑排序，因此不可能选取所有课程进行学习。
 * 通过 DFS 进行拓扑排序 - 一个关于Coursera的精彩视频教程（21分钟），介绍拓扑排序的基本概念。
 * 拓扑排序也可以通过 BFS 完成。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/course-schedule-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 题解：深度优先算法
    // 尝试列举每个课程的依赖线，只要包含了所有课程，就是OK的
    // 如果所有依赖线都是循环，则返回空数组
    public static int[] findOrder(int numCourses, int[][] prerequisites) {

        // 此课程依赖的课程
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i=0;i<prerequisites.length;i++) {
            List<Integer> cs = map.get(prerequisites[i][0]);
            if (cs == null) {
                cs = new ArrayList<>();
                map.put(prerequisites[i][0], cs);
            }
            cs.add(prerequisites[i][1]);
        }

        // 课程标记，索引表示课程，值表示状态：0-未搜索，1-搜索中，2-已搜索
        int[] flags = new int[numCourses];

        List<Integer> result = new ArrayList<>();

        for (int course=0;course<numCourses;course++) {
            dg(course, result, map, flags);
        }

        if (result.size() != numCourses) {
            return new int[0];
        }
        int[] r = new int[numCourses];
        for(int i=0;i<numCourses;i++) {
            r[i] = result.get(i);
        }
        return r;
    }

    private static boolean dg (int course, List<Integer> result, Map<Integer, List<Integer>> map, int[] flags) {
        if (flags[course] == 2) {
            return true;
        }
        if (flags[course] == 1) {
            // 当前搜索的课程正在搜索中，表示出现了循环
            flags[course] = 2;
            return false;
        }
        // 置为搜索中
        flags[course] = 1;

        List<Integer> integers = map.get(course);
        if (integers == null) {
            // 不依赖任何课程，直接结束，放入结果集，并把状态位置为已搜索
            result.add(course);
            flags[course] = 2;
            return true;
        }
        for (Integer c : integers) {
            if (!dg (c, result, map, flags)) {
                return false;
            }
        }
        flags[course] = 2;
        result.add(course);
        return true;
    }

    public static void main(String[] args) {
//        int[][] data = new int[][]{{1,0},{2,0},{3,1},{3,2}};
//        System.out.println(JSON.toJSONString(findOrder(4, data)));

        System.out.println(JSON.toJSONString(findOrder(2, new int[][]{{0, 1}, {1, 0}})));
    }
}
