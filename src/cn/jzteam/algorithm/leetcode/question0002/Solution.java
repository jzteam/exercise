package cn.jzteam.algorithm.leetcode.question0002;


/**
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 *
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 *
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 *
 * 示例：
 *
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-two-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class Solution {
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = null;
        ListNode temp = null;
        ListNode tempL1 = l1;
        ListNode tempL2 = l2;
        boolean up = false;
        do {
            int sum = tempL1.val + tempL2.val + (up ? 1 : 0);
            if (temp == null) {
                head = new ListNode(sum % 10);
                temp = head;
            } else {
                temp = (temp.next = new ListNode(sum % 10));
            }
            if (sum > 9) {
                up = true;
            } else {
                up = false;
            }
        } while ((tempL1 = tempL1.next) != null & (tempL2 = tempL2.next) != null);
        ListNode ln = null;
        if (tempL1 != null) {
            ln = tempL1;
        } else if (tempL2 != null) {
            ln = tempL2;
        }
        if (!up) {
            temp.next = ln;
            return head;
        } else {
            if (ln != null) {
                do {
                    int sum = ln.val + (up ? 1 : 0);
                    temp = (temp.next = new ListNode(sum % 10));
                    if (sum > 9) {
                        up = true;
                    } else {
                        up = false;
                    }
                } while ((ln = ln.next) != null && up);
            }
            if (ln != null) {
                temp.next = ln;
            }
            if (up) {
                temp.next = new ListNode(1);
            }
            return head;
        }
    }

    public static void main(String[] args) {
        ListNode h1 = null;
        ListNode h2 = null;
        ListNode l1 = null;
        ListNode l2 = null;
        int[] i1 = {9};
        int[] i2 = {1,9,9,9,9,9,8,9,9,9};
        for (int i = 0; i<i1.length; i++) {
            if (l1 == null) {
                h1 = new ListNode(i1[i]);
                l1 = h1;
            } else {
                l1.next = new ListNode(i1[i]);
            }
            if (l1.next != null) {
                l1 = l1.next;
            }
        }
        for (int i = 0; i<i2.length; i++) {
            if (l2 == null) {
                h2 = new ListNode(i2[i]);
                l2 = h2;
            } else {
                l2.next = new ListNode(i2[i]);
            }
            if (l2.next != null) {
                l2 = l2.next;
            }
        }
        ListNode result = addTwoNumbers(h1, h2);
        do {
            System.out.print(result.val);
        } while ((result = result.next) != null);
    }
}
