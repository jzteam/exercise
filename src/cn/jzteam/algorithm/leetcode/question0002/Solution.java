package cn.jzteam.algorithm.leetcode.question0002;


/**
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode(int x) { val = x; }
 * }
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
class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}