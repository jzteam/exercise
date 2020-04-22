package cn.jzteam.algorithm.leetcode.question0199;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一棵二叉树，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
 *
 * 示例:
 *
 * 输入: [1,2,3,null,5,null,4]
 * 输出: [1, 3, 4]
 * 解释:
 *
 *    1            <---
 *  /   \
 * 2     3         <---
 *  \     \
 *   5     4       <---
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/binary-tree-right-side-view
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        result.add(root.val);
        dg(result, 1, root);
        return result;
    }
    private static void dg(List<Integer> result, int index, TreeNode parentNode) {
        // 对应这层级，先放左，后放右
        if (parentNode.left != null) {
            if (index > result.size() - 1) {
                result.add(parentNode.left.val);
            } else {
                result.set(index, parentNode.left.val);
            }
            dg(result, index+1, parentNode.left);
        }
        if (parentNode.right != null) {
            if (index > result.size() - 1) {
                result.add(parentNode.right.val);
            } else {
                result.set(index, parentNode.right.val);
            }
            dg(result, index+1, parentNode.right);
        }
    }
}

