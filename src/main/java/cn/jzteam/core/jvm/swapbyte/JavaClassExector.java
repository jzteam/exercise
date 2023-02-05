package cn.jzteam.core.jvm.swapbyte;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 根据byte[]加载类，并反射实例化一个对象调用其main方法
 * 
 * @author Administrator
 *
 */
public class JavaClassExector {
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String execute(byte[] bytes){
        HackSystem.clearBuffer();
        
        // 替换java/lang/System
        ClassModifier cm = new ClassModifier(bytes);
//        byte[] targetBytes = cm.modifyUTF8Constant("java/lang/System", "cn.jzteam.core/jvm/swapbyte/HackSystem");
        byte[] targetBytes = cm.modifyUTF8Constant("cn/jzteam/core/jvm/swapbyte/Base", "cn/jzteam/core/jvm/swapbyte/Gao");
        
        HotSwapClassLoader loader = new HotSwapClassLoader();
        Class clazz = loader.loadByte(targetBytes);
        
        try {
            // 获取main方法
            Method method = clazz.getMethod("main", String[].class);
            // 执行静态方法不需要obj
            method.invoke(null, new String[]{null});
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return HackSystem.getBufferString();
    }
    
    public static void test(byte[] bytes){
    	// 替换java/lang/System
        ClassModifier cm = new ClassModifier(bytes);
        byte[] targetBytes = cm.modifyUTF8Constant("java/lang/System11", "cn/jzteam/core/jvm/swapbyte/HackSystem11");
        
    }
    
    public static void main(String[] args) throws IOException {
    	System.out.println("dfdafdafd");
        byte[] bytes = null;
        try {
        	// /Users/oker/git/exercise/target/classes/cn.jzteam.core/jvm/swapbyte/EexceuteTest.class
        	// G:\\code\\repository\\exercise\\target\\classes\\main.java.cn\\jzteam\\jvm\\swapbyte\\EexceuteTest.class
        	// /Users/oker/git/exercise/target/classes/cn.jzteam.core/jvm/swapbyte/VirtualTest.class
            FileInputStream in = new FileInputStream("/Users/oker/git/exercise/target/classes/cn.jzteam.core/jvm/swapbyte/VirtualTest.class");
            int available = in.available();
            System.out.println("class文件字节长度："+available);
            bytes = new byte[available];
            int read = in.read(bytes);
            in.close();
        } catch (Exception e) {
            e.printStackTrace(HackSystem.out);
        }
        
        String execute = execute(bytes);
//        test(bytes);
        
        System.out.println("执行完成："+execute);
    }

}
