package cn.jzteam.algorithm.leetcode.question0003;

/**
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 *
 * 示例 1:
 *
 * 输入: "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 示例 2:
 *
 * 输入: "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 示例 3:
 *
 * 输入: "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    /*
        从头开始检索，只要发现重复，就起点设置到 起点之后的第一个重复字符，继续检索
     */
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
