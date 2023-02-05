package cn.jzteam.algorithm.crc;

import java.util.HashMap;
import java.util.Map;

/**
 * 模二运算
 * 没有进位和借位的四则运算
 */
public class ModBinaryUtil {

    /**
     * 模二运算-加法
     * 0 + 0 = 0
     * 0 + 1 = 1
     * 1 + 1 = 0
     * 没有进位，相当于异或运算
     * @param x
     * @param y
     * @return
     */
    public static int mb_add(int x, int y){
        return x ^ y;
    }

    /**
     * 模二运算-减法
     * 0 - 0 = 0
     * 1 - 0 = 1
     * 0 - 1 = 1
     * 1 - 1 = 0
     * 没有借位，相当于异或运算
     * @param x
     * @param y
     * @return
     */
    public static int mb_subtract(int x, int y){
        return x ^ y;
    }

    /**
     * 模二运算-乘法
     * 被乘数的每一位与乘数相乘得到一个中间结果
     *     0 * 0 = 0
     *     1 * 0 = 0
     *     1 * 1 = 1
     * 最后将已产生的若干个中间结果使用模二加法得到最终结果
     *     
     * @param x
     * @param y
     * @return
     */
    public static int mb_multiply(int x, int y){
        int len = 0;
        int result = 0;
        while(y > 0){
            // 除 2 取余，得到的是二进制最后一位的值（0或1）
            int temp = y % 2;
            y = y / 2;
            // 记录当前 temp 是 y 的二进制的第几位
            len ++;
            // 如果该位是1，则表示中间结果就是 x<<len
            if(temp == 1) {
                // 将中间结果逐次累加
                result = mb_add(result, x<<len);
            }
        }
        return result;
    }
    
    enum EnumCRC {
        CRC8 (8 + 1, 0, "10011000"),
        CRC12 (12 + 1, 0, "10011000"),
        CRC16 (16 + 1, 0, "10011000"),
        CRC_CCITT (16 + 1, 0, ""),
        CRC32 (32 + 1, 0, "10011000");
        
        // 位数
        private int width;
        // 值
        private int value;
        // 二进制字符串
        private String desc;
        
        EnumCRC(final int width, final int value, final String desc) {
            this.width = width;
            this.value = value;
            this.desc = desc;
        }

        public int getWidth() {
            return this.width;
        }

        public int getValue() {
            return this.value;
        }
        
        public String getDesc() {
            return this.desc;
        }

        public static EnumCRC getEnum(Integer code) {
            return CODE_MAP.get(code);
        }

        private static final Map<Integer, EnumCRC> CODE_MAP = new HashMap<>();

        static {
            for (EnumCRC typeEnum : EnumCRC.values()) {
                CODE_MAP.put(typeEnum.getWidth(), typeEnum);
            }
        }
    }

    /**
     * 模二运算-除法
     * @param x
     * @param crcType
     * @return
     */
    public static int mb_divide(int x, EnumCRC crcType){
        // 被除数数组
        final byte[] divided = convertInt2Array(x, null);
        
        return 0;
    }

    /**
     * 将 数字 的二进制位 转换成 数组
     * @param x
     * @param crcType
     * @return
     */
    private static byte[] convertInt2Array(int x, EnumCRC crcType){
        int width = crcType != null ? crcType.getWidth() : 32;
        byte[] temp = new byte[width];
        int index = 0;
        while (x > 0) {
            // x 是int型不会超过32位，所以没有校验 index > width 的异常情况
            temp[index++] = (byte) (x % 2);
            x = x / 2;
        }
        byte[] result = new byte[width];
        for(int i = 0; index >= 0; index--){
            result[i] = temp[index];
        }

        return result;
    }

    /**
     * 将 二进制位数组 转换成 数字
     * @param array
     * @return
     */
    private static int convertArray2Int(byte[] array) {
        if(array.length > 32) {
            // 异常情况
            return -1;
        }
        int result = 0;
        for(int i = 0; i<array.length; i++){
            if(array[i] == 1) {
                result = result + 1<<i;
            }
        }
        return result;
    }
}
