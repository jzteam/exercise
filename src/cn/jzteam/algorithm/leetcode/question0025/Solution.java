package cn.jzteam.algorithm.leetcode.question0025;

import cn.jzteam.algorithm.leetcode.bean.ListNode;

/** K 个一组翻转链表
 * 给你一个链表，每 k 个节点一组进行翻转，请你返回翻转后的链表。
 *
 * k 是一个正整数，它的值小于或等于链表的长度。
 *
 * 如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 *
 *  
 *
 * 示例：
 *
 * 给你这个链表：1->2->3->4->5
 *
 * 当 k = 2 时，应当返回: 2->1->4->3->5
 *
 * 当 k = 3 时，应当返回: 3->2->1->4->5
 *
 *  
 *
 * 说明：
 *
 * 你的算法只能使用常数的额外空间。
 * 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-nodes-in-k-group
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 个人方案：栈。每进入k个，开始出栈，进行挂接的时候就是翻转的过程。
    // 栈深度为k，大小不固定，所以直接复用链表来实现栈的操作。
    public static ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k == 0 || k == 1) {
            return head;
        }
        // 用于返回整个链表，需要记录链头
        ListNode result = null;
        // 每k个翻转后的链表需要挂在总链表上，tail就是记录这个挂载的位置
        ListNode tail = null;
        // 游标
        ListNode pos = head;
        // 先统计链表长度
        int count = 1;
        while ((pos = pos.next) != null) count++;
        count = count / k;
        while (count-- > 0) {
            // 进行翻转
            ListNode start = head;
            ListNode end = start;
            head = head.next;
            end.next = null;

            int i=k-1;
            ListNode temp;
            while (i-- > 0) {
                // 将head放在reverse，并把原来的reverse挂在当前reverse下
                temp = head;
                head = head.next;
                temp.next = start;
                start = temp;
            }
            if (result == null) {
                result = start;
            } else {
                tail.next = start;
            }
            tail = end;
        }
        if (head != null) {
            tail.next = head;
        }

        return result;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        l1.next = l2;
        ListNode l3 = new ListNode(3);
        l2.next = l3;
        ListNode l4 = new ListNode(4);
        l3.next = l4;
        ListNode l5 = new ListNode(5);
        l4.next = l5;
        ListNode listNode = reverseKGroup(l1, 5);
        while (listNode != null) {
            System.out.print(listNode.val + " -> ");
            listNode = listNode.next;
        }
    }
}
