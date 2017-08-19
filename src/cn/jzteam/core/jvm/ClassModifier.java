package cn.jzteam.core.jvm;

/**
 * 劫持class字节码，修改常量池中的符号引用
 * 
 * @author Administrator
 *
 */
public class ClassModifier {
    
    // class字节码中常量池的偏移量
    private static final int CONSTANT_POOL_COUNT_INDEX = 8;
    
    // CONSTANT_UTF8_INFO的tag标记值
    private static final int CONSTANT_UTF8_INFO_TAG = 1;
    
    // 常量池中11中常量所占长度，除了utf8，其他类型的长度都是固定的（他们都是引用utf8）。Integer的tag=3
    private static final int[] CONSTANT_ITEM_LENGTH = {-1,-1,-1,5,5,9,9,3,3,5,5,5,5};
    
    
    private static final int u1 = 1;
    private static final int u2 = 2;
    
    private byte[] classBytes;
    
    public ClassModifier(byte[] classByte){
        this.classBytes = classByte;
    }
    
    public byte[] modifyUTF8Constant(String oldStr,String newStr){
        System.out.println("执行替换class字节码：oldStr="+oldStr+",newStr="+newStr);
        
        // 常量池大小（常量的个数）
        int count = ByteUtil.bytes2Int(classBytes, CONSTANT_POOL_COUNT_INDEX, u2);
        System.out.println("常量池一共 "+count+" 项");
        
        // 越过数量2个字节，来到第一个常量表index
        int offset = CONSTANT_POOL_COUNT_INDEX + u2;
        
        for(int i = 1;i<=count;i++){
            // 常量表，第一个字节是tag
            int tag = ByteUtil.bytes2Int(classBytes, offset, u1);
            System.out.println("#"+i+",tag="+tag);
            
            if(tag == CONSTANT_UTF8_INFO_TAG){
                // 常量类型是utf8
                //System.out.println("本次是utf8：tag="+tag);
                // 越过tag字节，接着两个字节是常量的字节长度
                int len = ByteUtil.bytes2Int(classBytes, offset + u1, u2);
                String str = ByteUtil.bytes2String(classBytes, offset + u1 + u2, len);
                System.out.println("#"+i+",str="+str+",len="+len);
                if(str.equalsIgnoreCase(oldStr)){
                    // 那么多utf8，找到需要替换的那种字符串
                    byte[] newStrBytes = ByteUtil.string2Bytes(newStr);
                    byte[] lenBytes = ByteUtil.int2Bytes(newStrBytes.length, u2);
                    //System.out.println("替换字符串的字节长度：len="+newStrBytes.length);
                    classBytes = ByteUtil.bytesReplace(classBytes, offset + u1, u2, lenBytes);
                    classBytes = ByteUtil.bytesReplace(classBytes, offset + u1 + u2, len, newStrBytes);
                    System.out.println("替换ok，i="+i);
                    //return classBytes;
                }else{
                    //System.out.println("不是要替换的字符串："+str);
                    offset += (u1+u2+len);
                }
                
            }else{
                // 常量类型不是utf8
                //System.out.println("本次不是utf8：tag="+tag);
                offset += CONSTANT_ITEM_LENGTH[tag];
            }
            
            // 查看#24
            if(i == 24){
                System.out.println("count==24时，tag="+tag);
            }
            
        }
       System.err.println("没有替换"); 
       return classBytes;
    }

}
