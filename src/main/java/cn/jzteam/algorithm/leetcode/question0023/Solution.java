package cn.jzteam.algorithm.leetcode.question0023;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 合并 k 个排序链表，返回合并后的排序链表。请分析和描述算法的复杂度。
 *
 * 示例:
 *
 * 输入:
 * [
 *   1->4->5,
 *   1->3->4,
 *   2->6
 * ]
 * 输出: 1->1->2->3->4->4->5->6
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/merge-k-sorted-lists
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // 两个有序链表合并，双指针
    // k个有序链表，则一直两两合并
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }
        Queue<ListNode> queue = new ArrayBlockingQueue<>(lists.length);
        for (ListNode ln : lists) {
            if (ln != null) {
                queue.add(ln);
            }
        }
        while (queue.size() >= 2) {
            ListNode l1 = queue.poll();
            ListNode l2 = queue.poll();
            queue.add(mergeTwo(l1, l2));
        }
        return queue.poll();
    }

    public static ListNode mergeTwo (ListNode l1, ListNode l2) {
        ListNode pos1 = l1;
        ListNode pos2 = l2;
        ListNode head;
        ListNode tail;
        if (pos1.val <= pos2.val) {
            head = pos1;
            pos1 = pos1.next;
        } else {
            head = pos2;
            pos2 = pos2.next;
        }
        tail = head;
        while (pos1 != null && pos2 != null) {
            if (pos1.val <= pos2.val) {
                tail.next = pos1;
                tail = pos1;
                pos1 = pos1.next;
            } else {
                tail.next = pos2;
                tail = pos2;
                pos2 = pos2.next;
            }
        }
        if (pos1 != null) {
            tail.next = pos1;
        }
        if (pos2 != null) {
            tail.next = pos2;
        }
        return head;
    }

    // 优先队列
    public static ListNode mergeKLists1(ListNode[] lists) {
        // 有序队列
        Queue<ListNode> queue = new PriorityQueue<>((ListNode o1, ListNode o2) -> {
                if (o1 != null && o2 != null) {
                if (o1.val < o2.val) {
                    return -1;
                } else if (o1.val == o2.val) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 0;
            }
        });
        for (ListNode ln : lists) {
            if (ln != null) {
                queue.add(ln);
            }
        }
        if (queue.isEmpty()) {
            return null;
        }

        ListNode head = queue.poll();
        if (head.next != null) {
            queue.add(head.next);
        }
        ListNode tail = head;
        while (!queue.isEmpty()) {
            tail = tail.next =queue.poll();
            if (tail.next != null) {
                queue.add(tail.next);
            }
        }
        return head;
    }

    public static void main(String[] args) {
        ListNode[] lists = new ListNode[3];
        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(5);
        ListNode l2 = new ListNode(1);
        l2.next = new ListNode(3);
        l2.next.next = new ListNode(4);
        ListNode l3 = new ListNode(2);
        l3.next = new ListNode(6);
        lists[0] = l1;
        lists[1] = l2;
        lists[2] = l3;

        ListNode listNode = mergeKLists1(lists);
        while (listNode != null) {
            System.out.print(listNode.val + ", ");
            listNode = listNode.next;
        }
    }
}
