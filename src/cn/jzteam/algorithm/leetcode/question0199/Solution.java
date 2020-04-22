package cn.jzteam.algorithm.leetcode.question0199;

import com.alibaba.fastjson.JSON;

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

    public static List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        result.add(root.val);
        dg(result, 1, root);
        return result;
    }
    private static void dg(List<Integer> result, int index, TreeNode parentNode) {
        // 对应这层级，先放右，后放左。（如果这层级已有元素，一定是右，左就不要set了）
        // 如果list长度撑开了，表示每一层级一定都是最靠右的元素，不用再设置了
        if (parentNode.right != null) {
            if (index > result.size() - 1) {
                result.add(parentNode.right.val);
            }
            dg(result, index+1, parentNode.right);
        }
        if (parentNode.left != null) {
            if (index > result.size() - 1) {
                result.add(parentNode.left.val);
            }
            dg(result, index+1, parentNode.left);
        }
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.right = new TreeNode(5);
        root.right.right = new TreeNode(4);
        System.out.println(JSON.toJSONString(rightSideView(root)));
    }
}

