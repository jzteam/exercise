package cn.jzteam.algorithm.leetcode.question0003;

public class Solution {
    public static int lengthOfLongestSubstring(String s) {
        // 一旦出现重复就要从（pre + 1）重新开始计算，直到终点
        int max = 0;
        int start = 0;
        for (int i = 0; i<s.length(); i++) {
            char c = s.charAt(i);
            // 上一个字符的索引，从start开始检索
            int pre = s.indexOf(c, start);
            // 发现重复，则重置start。start之后检索c，最不济也是当前i
            if (pre < i){
                start = pre + 1;
            }
            if (i - start + 1 > max) {
                max = i - start + 1;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int result = lengthOfLongestSubstring("abcabcbb");
        System.out.println("result=" + result);
    }
}
