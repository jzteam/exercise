package cn.jzteam.algorithm.leetcode.question0572;

import java.util.Stack;

/**
 * 给定两个非空二叉树 s 和 t，检验 s 中是否包含和 t 具有相同结构和节点值的子树。s 的一个子树包括 s 的一个节点和这个节点的所有子孙。s 也可以看做它自身的一棵子树。
 *
 * 示例 1:
 * 给定的树 s:
 *
 *      3
 *     / \
 *    4   5
 *   / \
 *  1   2
 * 给定的树 t：
 *
 *    4
 *   / \
 *  1   2
 * 返回 true，因为 t 与 s 的一个子树拥有相同的结构和节点值。
 *
 * 示例 2:
 * 给定的树 s：
 *
 *      3
 *     / \
 *    4   5
 *   / \
 *  1   2
 *     /
 *    0
 * 给定的树 t：
 *
 *    4
 *   / \
 *  1   2
 * 返回 false。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/subtree-of-another-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 暴力破解，s每一个节点都可能是t的root
    // DFS 深度优先：O(s) * O(t) = O(s*t)
    public static boolean isSubtree1(TreeNode s, TreeNode t) {
        // 先找到所有跟t.val相等的节点，因为他们都有可能跟t完全相同
        Stack<TreeNode> stack = new Stack<>();
        dfs(stack, s, t.val);


        // 再逐个判断，stack中的节点是不是跟t完全一致
        while (!stack.isEmpty()) {
            TreeNode pop = stack.pop();
            // 有一个节点成功，就可直接返回
            if (isSame(pop, t)) {
                return true;
            }
        }
        return false;
    }

    public static void dfs (Stack<TreeNode> stack, TreeNode node, int val) {
        if (node == null) {
            return;
        }
        // 先左
        if (node.left != null) {
            dfs(stack, node.left, val);
        }
        // 再右
        if (node.right != null) {
            dfs(stack, node.right, val);
        }
        // 最后中间
        if (node.val == val) {
            stack.push(node);
        }
    }

    public static boolean isSame (TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) {
            return true;
        }
        if (t1 != null && t2 != null) {
            // 自己相等，左边相等，右边也相等，才返回true
            return t1.val == t2.val && isSame(t1.left, t2.left) && isSame(t1.right, t2.right);
        } else {
            return false;
        }
    }

    // 转换成DFS序列，然后做字符串匹配
    // 字符串匹配可以使用 暴力匹配 O(s*t), KMP 或者 Rabin-Karp 算法 O(s+t)
    public static boolean isSubtree2(TreeNode s, TreeNode t) {
        // 每个节点在树中的位置是固定的，那么该节点代表的子树在树中的位置也是固定的
        // 如果转化成字符串，在整棵树中的位置就是该节点所在的那个位置
        // 所以节点的匹配可以转化成字符串的匹配
        // 转化规则要一致，而且要考虑边界问题，即只有一个孩子节点时，无法区分是左还是右。
        // 所以给所有缺失孩子节点的节点增加 lnull 或者 rnull，解决上述问题
        StringBuffer ssb = new StringBuffer("-");
        dfs(ssb, s);

        StringBuffer tsb = new StringBuffer("-");
        dfs(tsb, t);

        return ssb.toString().contains(tsb.toString());
    }

    public static void dfs (StringBuffer sb, TreeNode node) {
        if (node == null) {
            // 递归方法中不会传入null，只有起点会传入null，啥都不做
            return;
        }
        // 先左
        if (node.left != null) {
            dfs(sb, node.left);
        } else {
            sb.append("lnull-");
        }
        // 再右
        if (node.right != null) {
            dfs(sb, node.right);
        } else {
            sb.append("rnull-");
        }
        // 最后中间
        sb.append(node.val + "-");
    }

    // 哈希
    // 将每一个节点都做成子树的哈希值，包含本身val、子树hash、子节点val、左右子树权值
    // 有重复的概率，但是可以通过多次哈希降低概率，也算是解决问题办法，但不是理论绝对正确
    public static boolean isSubtree3(TreeNode s, TreeNode t) {

        return false;
    }

    public static void main(String[] args) {
        TreeNode s = new TreeNode(3);
        s.left = new TreeNode(4);
        s.right = new TreeNode(5);
        s.left.left = new TreeNode(1);
        s.left.right = new TreeNode(2);

        TreeNode t = new TreeNode(4);
        t.left = new TreeNode(1);
        t.right = new TreeNode(2);

        System.out.println(isSubtree2(s, t));
        s.left.right.left = new TreeNode(0);
        System.out.println(isSubtree2(s, t));

        TreeNode s1 = new TreeNode(1);
        s1.left = new TreeNode(1);

        TreeNode t1 = new TreeNode(1);
        System.out.println(isSubtree2(s1, t1));

    }
}
