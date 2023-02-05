package cn.jzteam.algorithm.leetcode.question0098;

import java.util.Stack;

/**
 * 给定一个二叉树，判断其是否是一个有效的二叉搜索树。
 *
 * 假设一个二叉搜索树具有如下特征：
 *
 * 节点的左子树只包含小于当前节点的数。
 * 节点的右子树只包含大于当前节点的数。
 * 所有左子树和右子树自身必须也是二叉搜索树。
 * 示例 1:
 *
 * 输入:
 *     2
 *    / \
 *   1   3
 * 输出: true
 * 示例 2:
 *
 * 输入:
 *     5
 *    / \
 *   1   4
 *      / \
 *     3   6
 * 输出: false
 * 解释: 输入为: [5,1,4,null,null,3,6]。
 *      根节点的值为 5 ，但是其右子节点值为 4 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/validate-binary-search-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // 题解：中序遍历 O(n)时间和空间
    // 处理顺序是优先左，其次中，最后右，按照这个顺序如果满足单调递增，就是搜索二叉树的结构
    // 使用栈，压入每一个左节点，当没有左节点，就开始弹出取值，操作完成之后，压入右边节点，并从此节点再次开始压入每一个左节点
    public static boolean isValidBST(TreeNode root) {
        // 单调递增的数值游标，起点必须是已知范围的最小的值
        double former = -Double.MAX_VALUE;
        Stack<TreeNode> stack = new Stack<>();
        while (!stack.isEmpty() || root != null) {
            // 只要当前节点不为空，就压入栈，并深入获取其左节点
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            // 内循环结束，表示左节点为空，弹出该子节点，处理其值，并深入探索其右节点
            root = stack.pop();
            if (root.val <= former) {
                return false;
            }
            former = root.val;
            root = root.right;
        }
        return true;
    }

    // 题解：递归
    // 二叉树的深度就是递归的最大深度
    public static boolean isValidBST1(TreeNode root) {
        if (root == null) {
            return true;
        }
        return dg(root, -Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public static boolean dg (TreeNode node, double leftLimit, double rightLimit) {
        // 左侧不合法，直接返回
        if (node.left != null && !dg (node.left, leftLimit, node.val)) {
            return false;
        }
        // 不存在左节点，或者左侧全部合法，则判断右侧
        if (node.right != null && !dg(node.right, node.val, rightLimit)) {
            return false;
        }
        if (node.val <= leftLimit || node.val >= rightLimit) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(Double.MIN_VALUE); // 大于0
        System.out.println(-Double.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE <= 0); // 小于0
        TreeNode treeNode = new TreeNode(-2147483648); // true
        System.out.println(isValidBST1(treeNode));

        TreeNode treeNode1 = new TreeNode(5);
        treeNode1.left = new TreeNode(1);
        treeNode1.right = new TreeNode(4);
        treeNode1.right.left = new TreeNode(3);
        treeNode1.right.right = new TreeNode(6);
        System.out.println(isValidBST1(treeNode1));
    }
}
