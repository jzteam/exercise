package cn.jzteam.algorithm.leetcode.question0102;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/** 二叉树的层序遍历
 * 给你一个二叉树，请你返回其按 层序遍历 得到的节点值。 （即逐层地，从左到右访问所有节点）。
 *
 *  
 *
 * 示例：
 * 二叉树：[3,9,20,null,null,15,7],
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回其层次遍历结果：
 *
 * [
 *   [3],
 *   [9,20],
 *   [15,7]
 * ]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/binary-tree-level-order-traversal
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    public static List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<List<Integer>> result = new ArrayList<>();

        LinkedTreeNode head = new LinkedTreeNode(root, null);
        LinkedTreeNode tail = head;
        int pos = 0;
        int num = 1;

        List<Integer> temp = new ArrayList<>();
        while (head != null) {
            TreeNode t = head.val;
            temp.add(t.val);
            if (t.left != null) {
                tail.next = new LinkedTreeNode(t.left, null);
                tail = tail.next;
                pos++;
            }
            if (t.right != null) {
                tail.next = new LinkedTreeNode(t.right, null);
                tail = tail.next;
                pos++;
            }
            head = head.next;
            num--;
            if (num == 0) {
                result.add(temp);
                temp = new ArrayList<>();
                num = pos;
                pos = 0;
            }
        }
        return result;
    }

    static class LinkedTreeNode {
        TreeNode val;
        LinkedTreeNode next;
        LinkedTreeNode(TreeNode node, LinkedTreeNode next) {
            this.val = node;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        TreeNode t = new TreeNode(3);
        t.left = new TreeNode(9);
        t.right = new TreeNode(20);
        t.right.left = new TreeNode(15);
        t.right.right = new TreeNode(7);
        System.out.println(JSON.toJSONString(levelOrder(t)));
    }
}
