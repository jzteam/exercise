package cn.jzteam.algorithm.leetcode.question0236;

/**
 * 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
 *
 * 百度百科中最近公共祖先的定义为：“对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
 *
 * 例如，给定如下二叉树:  root = [3,5,1,6,2,0,8,null,null,7,4]
 *
 *
 *
 *  
 *
 * 示例 1:
 *
 * 输入: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 * 输出: 3
 * 解释: 节点 5 和节点 1 的最近公共祖先是节点 3。
 * 示例 2:
 *
 * 输入: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
 * 输出: 5
 * 解释: 节点 5 和节点 4 的最近公共祖先是节点 5。因为根据定义最近公共祖先节点可以为节点本身。
 *  
 *
 * 说明:
 *
 * 所有节点的值都是唯一的。
 * p、q 为不同节点且均存在于给定的二叉树中。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 社区答案：递归寻找p/q相等的节点，只要找到就向上抛
    // 最终收敛到root节点时，左边和右边都有返回，说明root的左右各一个，最近公共祖先就是root
    // 只要p/q都在同一侧，另一侧就一定是null，依据这个道理就可以递归搜索了
    public static TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        if (root.val == p.val || root.val == q.val) {
            return root;
        }
        TreeNode left = lowestCommonAncestor2(root.left, p, q);
        TreeNode right = lowestCommonAncestor2(root.right, p, q);
        if (left != null && right != null) {
            // 当前节点root的左分支和右分支都找到了p/q，说明root就是分叉的地方，即最近公共祖先
            return root;
        } else if (left != null) {
            // 左分支返回了一个匹配上的最近节点，右分支没有匹配上，说明p/q一定都在左分支这边
            return left;
        } else if (right != null) {
            // 右分支返回了一个匹配上的最近节点，左分支没有匹配上，说明p/q一定都在右分支这边
            return right;
        }
        return null;
    }

    // 个人方案：深度优先遍历，Stack中每一刻保存的都是从根节点到子节点的唯一路径，任何子节点的路径都会有重复的地方，至少是根节点
    // 找到第一个节点之后，开始记录深度最小值，直到找到第二个节点，返回深度最小值处的节点
    static TreeNode result = null;
    static int minDeep = 0;
    static int posDeep = 0;
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 类成员必须此处再次初始化为默认值，否则leetCode会重复利用一个对象，留存了测试用例的结果
        result = null;
        minDeep = 0;
        posDeep = 0;

        if (root == null) {
            return null;
        }
        if (p == null) return q;
        if (q == null) return p;
        dg(root, p, q);
        return result;
    }
    public static boolean dg (TreeNode root, TreeNode p, TreeNode q) {
        // 方法入栈，深度+1
        posDeep++;

        // 先判断
        if (root.val == p.val || root.val == q.val) {
            if (minDeep > 0) {
                // 此刻已经找到result
                return true;
            }
            minDeep = posDeep;
            result = root;
        }

        // 再深入左，最后深入右
        if ((root.left != null && dg (root.left, p, q))){
            return true;
        }
        // 只匹配到一个节点，再次返回这一层，就需要记录该深度
        if (minDeep > 0 && posDeep < minDeep) {
            // 找到过第一个节点后开始，记录方法栈深度最小的那个节点
            minDeep = posDeep;
            result = root;
        }
        if ((root.right != null) && dg (root.right, p, q)) {
            return true;
        }

        // 只匹配到一个节点，再次返回这一层，就需要记录该深度
        if (minDeep > 0 && posDeep < minDeep) {
            // 找到过第一个节点后开始，记录方法栈深度最小的那个节点
            minDeep = posDeep;
            result = root;
        }
        // 方法出栈，深度-1
        posDeep--;
        return false;
    }

    // 题解：递归
    // 递归方法 f 用于判断该节点有没有包含p/q，如果包含则有如下特点：
    // 节点自身就是p/q 或 左分支包含p/q 或 右分支包含p/q
    // 在递归过程中：
    //      如果发现当前节点，左右分支均包含p/q，则该节点一定是最近公共祖先。只有最近的祖先才会是一左一右均包含
    //      如果发现当前节点，自身就是p/q，而且其左分支或右分支包含p/q，则该节点一定是最近公共祖先
    // 如果遇到上述情况，通过全局变量记录这个节点，递归完成，直接返回这个节点即可。
    // 该思路的缺点是不收敛，一定是遍历全部节点。
    public static TreeNode lowestCommonAncestor1(TreeNode root, TreeNode p, TreeNode q) {
        f (root, p, q);
        return result;
    }

    public static boolean f (TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return false;
        boolean left = f (root.left, p, q);
        boolean right = f (root.right, p, q);
        if ((left && right) || ((root.val == p.val || root.val == q.val) && (left || right))) {
            result = root;
        }
        return root.val == p.val || root.val == q.val || left || right;
    }


    public static void main(String[] args) {
        TreeNode t = new TreeNode(3);

        t.left = new TreeNode(5);
        t.right = new TreeNode(1);

        t.left.left = new TreeNode(6);
        t.left.right = new TreeNode(2);

        t.right.left = new TreeNode(0);
        t.right.right = new TreeNode(8);

        t.left.right.left = new TreeNode(7);
        t.left.right.right = new TreeNode(4);

        System.out.println(lowestCommonAncestor1(t, new TreeNode(5), new TreeNode(4)).val);
    }

}
