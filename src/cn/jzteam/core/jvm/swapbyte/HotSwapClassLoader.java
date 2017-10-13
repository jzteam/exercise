package cn.jzteam.core.jvm.swapbyte;
/**
 * 自定义加载器，指定的父类加载器是这个类的加载器，那么它加载的类是在同一体系中的，可以相互识别
 * 
 * @author Administrator
 *
 */
public class HotSwapClassLoader extends ClassLoader{
    
    public HotSwapClassLoader(){
        super(HotSwapClassLoader.class.getClassLoader());
    }
    
    @SuppressWarnings("rawtypes")
    public Class loadByte(byte[] classBytes){
        return defineClass(null, classBytes, 0, classBytes.length);
    }

}
