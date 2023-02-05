package cn.jzteam.core.jvm.swapbyte;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    
    public static PrintStream out;
    
    static{
    	try {
			out = new PrintStream(new FileOutputStream("/Users/oker/work/t.txt"));
		} catch (FileNotFoundException e) {
		}
    }

    public final static OutputStream err = new PrintStream(buffer);
    
    public static String getBufferString(){
        return buffer.toString();
    }
    
    public static void clearBuffer(){
        buffer.reset();
    }
    
}
