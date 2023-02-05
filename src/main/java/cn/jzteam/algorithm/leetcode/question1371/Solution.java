package cn.jzteam.algorithm.leetcode.question1371;

/**
 * 给你一个字符串 s ，请你返回满足以下条件的最长子字符串的长度：每个元音字母，即 'a'，'e'，'i'，'o'，'u' ，在子字符串中都恰好出现了偶数次。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：s = "eleetminicoworoep"
 * 输出：13
 * 解释：最长子字符串是 "leetminicowor" ，它包含 e，i，o 各 2 个，以及 0 个 a，u 。
 * 示例 2：
 *
 * 输入：s = "leetcodeisgreat"
 * 输出：5
 * 解释：最长子字符串是 "leetc" ，其中包含 2 个 e 。
 * 示例 3：
 *
 * 输入：s = "bcbcbc"
 * 输出：6
 * 解释：这个示例中，字符串 "bcbcbc" 本身就是最长的，因为所有的元音 a，e，i，o，u 都出现了 0 次。
 *  
 *
 * 提示：
 *
 * 1 <= s.length <= 5 x 10^5
 * s 只包含小写英文字母。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-the-longest-substring-containing-vowels-in-even-counts
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 题解：前缀和 + 状态压缩
    // 前缀和：记录每个位置之前的每个元音出现的次数，那么只要 i->j 之间的各个元音数量是偶数，那么找到最大的 （j-i） 即可
    // 状态压缩：记录每个元音的次数消耗较大，可以进行压缩。因为记录此信息是为了相减得偶数，其实我们知道奇偶性相同的数相减一定得偶数，所以我们只需要保存奇偶性即可
    // 而且每个元音的奇偶性放在二进制中，可以组成一个int数字。aeiou 5个元音，用5个bit表示，int范围则是 0～31
    // 其实也不用每个位置都保存一下奇偶性，我们只需要记录32种奇偶性出现情况的最左边索引即可，然后随着字符的遍历，时刻更新相同奇偶性的最大间距
    public static int findTheLongestSubstring(String s) {
        // bit: 0-偶数，1-奇数
        // 异或，更新指定位置的奇偶性
        // a 10000 16
        // e 01000 8
        // i 00100 4
        // o 00010 2
        // u 00001 1
        int[] pos = new int[32];
        for (int i=0;i<32;i++) {
            pos[i] = -1;
        }
        int max = 0;
        int res = 0;
        for (int i=0;i<s.length();i++) {
            if (s.charAt(i) == 'a') {
                res ^= 16;
            } else if (s.charAt(i) == 'e') {
                res ^= 8;
            } else if (s.charAt(i) == 'i') {
                res ^= 4;
            } else if (s.charAt(i) == 'o') {
                res ^= 2;
            } else if (s.charAt(i) == 'u') {
                res ^= 1;
            }
            // 所有元音都是偶数的起始位置是-1，所以 pos[0]的值直接是-1，不要再覆盖了
            if (res > 0 && pos[res] == -1) {
                pos[res] = i;
            }
            max = Math.max(max, i-pos[res]);
        }
        return max;
    }


    public static void main(String[] args) {
        System.out.println(findTheLongestSubstring("leetcodeisgreat")); //5
        System.out.println(findTheLongestSubstring("eleetminicoworoep")); //13
        System.out.println(findTheLongestSubstring("bcbcbc")); //6
    }
}
