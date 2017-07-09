package cn.jzteam.jvm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        byte[] targetBytes = cm.modifyUTF8Constant("java/lang/System", "cn/jzteam/jvm/HackSystem");
        
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
    
    public static void main(String[] args) throws IOException {
        byte[] bytes = null;
        try {
            FileInputStream in = new FileInputStream("G:\\code\\repository\\exercise\\target\\classes\\cn\\jzteam\\jvm\\EexceuteTest.class");
            int available = in.available();
            System.out.println("class文件字节长度："+available);
            bytes = new byte[available];
            in.read(bytes);
            in.close();
        } catch (Exception e) {
            e.printStackTrace(HackSystem.out);
        }
        
        String execute = execute(bytes);
        
        System.out.println("执行完成："+execute);
    }

}
