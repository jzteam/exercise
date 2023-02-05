package cn.jzteam.algorithm.leetcode.question0006;

import java.util.ArrayList;
import java.util.List;

/**
 * 将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。
 *
 * 比如输入字符串为 "LEETCODEISHIRING" 行数为 3 时，排列如下：
 *
 * L   C   I   R
 * E T O E S I I G
 * E   D   H   N
 * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："LCIRETOESIIGEDHN"。
 *
 * 请你实现这个将字符串进行指定行数变换的函数：
 *
 * string convert(string s, int numRows);
 * 示例 1:
 *
 * 输入: s = "LEETCODEISHIRING", numRows = 3
 * 输出: "LCIRETOESIIGEDHN"
 * 示例 2:
 *
 * 输入: s = "LEETCODEISHIRING", numRows = 4
 * 输出: "LDREOEIIECIHNTSG"
 * 解释:
 *
 * L     D     R
 * E   O E   I I
 * E C   I H   N
 * T     S     G
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/zigzag-conversion
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    // 个人方案
    // 发现循环 2 * numRows - 2。Z字形连起来，就像是很多个V，而且第一个字符和中间一个字符不重复。
    public static String convert(String s, int numRows) {
        if(numRows <= 1 || s.length() <= 1) {
            return s;
        }
        List<StringBuffer> sbList = new ArrayList<>();
        for (int i=0;i<numRows;i++) {
            sbList.add(new StringBuffer());
        }
        for (int i=sbList.size()-2;i>=1;i--) {
            sbList.add(sbList.get(i));
        }
        int pos=0;
        while (pos < s.length()) {
            sbList.get(pos % (2*numRows-2)).append(s.charAt(pos));
            pos++;
        }
        StringBuffer result = new StringBuffer();
        for (int i=0;i<numRows;i++) {
            result.append(sbList.get(i));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(convert("LEETCODEISHIRING", 3)); // LCIRETOESIIGEDHN
        System.out.println(convert("LEETCODEISHIRING", 4)); // LDREOEIIECIHNTSG
    }
}
