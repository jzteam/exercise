package cn.jzteam.core.jvm;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 劫持java.lang.System常量，替换成自己的System
 * 
 * @author Administrator
 *
 */
public class HackSystem {
    
    public final InputStream in = System.in;
    
    public static ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    
    public final static PrintStream out = new PrintStream(buffer);

    public final OutputStream err = out;
    
    public static String getBufferString(){
        return buffer.toString();
    }
    
    public static void clearBuffer(){
        buffer.reset();
    }
    
}
