package cn.jzteam.algorithm.leetcode.question0032;

import java.util.Stack;

public class Solution {
    // 个人方案
    // 使用栈来匹配括号，有效的括号一定都能匹配出栈，剩下的都是无效的括号。
    // 栈中元素都表示括号的位置索引，无效括号索引差就是有效括号的子串长度。
    public static int longestValidParentheses(String s) {
        Stack<Integer> stack = new Stack<>();
        if (s.length() == 0) {
            return 0;
        }
        for (int i = 0; i<s.length(); i++) {
            if(stack.empty() || s.charAt(stack.peek()) >= s.charAt(i)) {
                // 相同表示不是一对，要入栈
                stack.push(i);
            } else {
                // 不同表示是一对有效括号，要出栈
                stack.pop();
            }
        }
        if (stack.empty()) {
            return s.length();
        }
        int max = 0;
        int end = s.length();
        int start = -1;
        int pop;
        while (!stack.empty()) {
            pop = stack.pop();
            max = Math.max(max, end - pop - 1);
            end = pop;
        }
        max = Math.max(max, end - start - 1);
        return max;
    }

    public static void main(String[] args) {
        System.out.println(longestValidParentheses("(()"));
        System.out.println(longestValidParentheses(")()())"));
        System.out.println(longestValidParentheses(")(())())"));
    }
}
