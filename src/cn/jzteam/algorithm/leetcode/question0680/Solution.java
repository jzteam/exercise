package cn.jzteam.algorithm.leetcode.question0680;

/**
 * 给定一个非空字符串 s，最多删除一个字符。判断是否能成为回文字符串。
 *
 * 示例 1:
 *
 * 输入: "aba"
 * 输出: True
 * 示例 2:
 *
 * 输入: "abca"
 * 输出: True
 * 解释: 你可以删除c字符。
 * 注意:
 *
 * 字符串只包含从 a-z 的小写字母。字符串的最大长度是50000。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/valid-palindrome-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 个人方案：双指针，回文字符串应该对称，如果有不对称的地方，删掉一边的字符，应该满足对称，否则就不满足条件
    public static boolean validPalindrome(String s) {
        int left = 0;
        int right = s.length()-1;
        while (s.charAt(left) == s.charAt(right) && ++left < --right);
        if (left >= right) {
            return true;
        }
        return validPalindrome(s, left+1, right) || validPalindrome(s, left, right-1);
    }

    public static boolean validPalindrome(String s, int left, int right) {
        while (s.charAt(left) == s.charAt(right) && ++left < --right);
        return left >= right;
    }



    public static void main(String[] args) {
        System.out.println(validPalindrome("aba"));
        System.out.println(validPalindrome("abcda"));
        System.out.println(validPalindrome("ebcbbececabbacecbbcbe")); // true
    }
}
