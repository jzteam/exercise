package cn.jzteam.algorithm.leetcode.question0005;

/**
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
 *
 * 示例 1：
 *
 * 输入: "babad"
 * 输出: "bab"
 * 注意: "aba" 也是一个有效答案。
 * 示例 2：
 *
 * 输入: "cbbd"
 * 输出: "bb"
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-palindromic-substring
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // Manacher 算法
    public static String longestPalindrome(String s) {

        return "";
    }

    // 动态规划
    public static String longestPalindrome1(String s) {
        // 如果 i -> j 是回文，那么 i-1 -> j-1 也一定是回文。(i<j)
        // 因为需要由内向外扩展，所以判断 Si+1 == Sj+1，那么 i+1 -> j+1 就是回文
        // 起始边界：
        // 一个字符的回文：i->i 一定是回文
        // 两个字符的回文：如果 Si == Si+1，那么 i->i+1 一定是回文
        // f(i,j) = (f(i+1,j-1) && Si == Sj)

        if (s.length() == 0) {
            return "";
        }
        int start = 0;
        int end = 0;
        int[][] f = new int[s.length()][s.length()];
        // 初始化边界：相当于矩形一根对角线，以及挨着对角线靠下的一条平行线，都已经初始化了
        for (int i=0;i<s.length(); i++) {
            f[i][i] = 1;
            if (i+1 < s.length() && s.charAt(i) == s.charAt(i+1)) {
                f[i][i+1] = 1;
                if (end == 0) {
                    start = i;
                    end = i+1;
                }
            }
        }
        // 因为 i 是依赖于 i+1，所以 i从大往小；j是依赖于j-1，所以j从小往大。每一个i循环都j=i为起点，直到j=s.length
        for (int i=s.length()-2; i>=0; i--) {
            // 因为 i与j 相差0和相差1的结果已经计算过了，所以这里的循环都是相差2及以上的，顾 j=i+2起步
            for (int j=i+2; j<=s.length()-1; j++) {
                f[i][j] = f[i+1][j-1] == 1 && s.charAt(i) == s.charAt(j) ? 1 : 0;
                if (f[i][j] == 1 && j-i > end-start) {
                    start = i;
                    end = j;
                }
            }
        }
        return s.substring(start, end + 1);
    }

    // 中心扩展
    public static String longestPalindrome2(String s) {
        // 起码有两个字符才能执行后面的
        if (s.length() <= 1) {
            return s;
        }

        int size = 0;
        int pos = 0;
        int n = 0; // 镜像中心，或者镜像中心偏左 的元素索引
        for (;n<=s.length();n++) {
            int i = 1;
            // 镜像中心
            while (n - i >= 0 && n + i < s.length() && s.charAt(n - i) == s.charAt(n + i)) {
                i++;
            }
            i--; // 保证i还是那个满足条件的i
            if (1 + i*2 > size) {
                size = 1 + i*2;
                pos = n;
            }
            // 镜像中心偏左
            if (n+1 < s.length() && s.charAt(n) == s.charAt(n + i)) {
                // 同样的i, 镜像中心长度比镜像偏左对应少1
                while (n - i >= 0 && n + i + 1 < s.length() && s.charAt(n - i) == s.charAt(n + i + 1)) {
                    i++;
                }
                i--; // 保证i还是那个满足条件的i
                if ((i + 1) * 2 > size) {
                    size = (i + 1) * 2;
                    pos = n;
                }
            }

        }
        if (size % 2 == 1) {
            // 奇数，pos就是镜像中心
            return s.substring(pos-size/2, pos + size/2 + 1);
        } else {
            // 偶数，pos就是镜像偏左
            return s.substring(pos-size/2 + 1, pos + size/2 + 1);
        }
    }

    // 最长公共子串
    public static String longestPalindrome3(String s) {

        return "";
    }

    public static void main(String[] args) {
        System.out.println(longestPalindrome1(""));
        System.out.println(longestPalindrome1("a"));
        System.out.println(longestPalindrome2("aaaaaa"));
        System.out.println(longestPalindrome2("aba"));
    }

}
