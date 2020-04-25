package cn.jzteam.algorithm.leetcode.question0046;

import avro.shaded.com.google.common.collect.Lists;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给定一个 没有重复 数字的序列，返回其所有可能的全排列。
 *
 * 示例:
 *
 * 输入: [1,2,3]
 * 输出:
 * [
 *   [1,2,3],
 *   [1,3,2],
 *   [2,1,3],
 *   [2,3,1],
 *   [3,1,2],
 *   [3,2,1]
 * ]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/permutations
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    private static List<List<Integer>> result;
    // 个人方案
    public static List<List<Integer>> permute(int[] nums) {
        result = new ArrayList<>();
        ArrayList target = new ArrayList<>();
        for (int i=0;i<nums.length;i++) {
            target.add(nums[i]);
        }
        setValue(target, 0);
        return result;
    }

    // 要为target的position位置设置元素，已经使用过的元素为target[0-position)
    private static void setValue(ArrayList<Integer> target, int position) {
//        if (position > target.size() - 1) {
//            return;
//        }
        if (position == target.size()-1) {
            result.add((ArrayList<Integer>)target.clone());
            return;
        }
        // 为position设值，取值只能是 target[position, length-1]
        for (int i=position; i<target.size(); i++) {
            // 交换
            int temp = target.get(position);
            target.set(position, target.get(i));
            target.set(i, temp);
            // 递归
            setValue(target, position + 1);
            // 复位
            target.set(i, target.get(position));
            target.set(position, temp);
        }
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(permute(new int[]{1})));
        System.out.println(JSON.toJSONString(permute(new int[]{1,2,3})));
    }
}
