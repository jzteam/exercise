package cn.jzteam.core.jvm;

/**
 * 字节操作
 * 
 * @author Administrator
 *
 */
public class ByteUtil {
    
    public static int bytes2Int(byte[] b, int start, int len){
        int sum = 0;
        int end = start + len;
        for(int i = start; i<end; i++){
            int n = ((int)b[i]) & 0xff;
            n <<= (--len)*8;
            sum += n;
        }
        return sum;
    }
    
    public static byte[] int2Bytes(int value,int len){
        byte[] b = new byte[len];
        for(int i=0;i<len;i++){
            b[len-i-1] = (byte)((value >> 8*i)& 0xff);
        }
        return b;
    }
    
    public static String bytes2String(byte[] b, int start, int len){
        return new String(b,start,len);
    }
    
    public static byte[] string2Bytes(String str){
        return str.getBytes();
    }
    
    public static byte[] bytesReplace(byte[] orgin,int offset,int len,byte[] replace){
        byte[] newBytes = new byte[orgin.length+replace.length-len];
        
        // copy 前部分
        System.arraycopy(orgin, 0, newBytes, 0, offset);
        // 插入中间部分
        System.arraycopy(replace, 0, newBytes, offset, replace.length);
        
        // copy 后部分
        System.arraycopy(orgin, offset+len, newBytes, offset+replace.length, orgin.length-offset-len);
        
        return newBytes;
    }

}
