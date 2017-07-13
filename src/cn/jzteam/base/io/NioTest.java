package cn.jzteam.base.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import org.junit.Test;

public class NioTest {
    private static final int BSIZE = 1024;

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        String path = "G:/study_test/base/nio/data.txt";
        String context = "首先要明白这道题目的考查点是什么，一是大家首先要对计算机原理的底层细节要清楚、要知道加减法的位运算原理和知道计算机中的算术运算会发生越界的情况，二是要具备一定的面向对象的设计思想。"
                + "首先，计算机中用固定数量的几个字节来存储的数值，所以计算机中能够表示的数值是有一定的范围的，为了便于讲解和理解，我们先以byte 类型的整数为例，它用1个字节进行存储，表示的最大数值范围为-128到+127。"
                + "-1在内存中对应的二进制数据为11111111，如果两个-1相加，不考虑Java运算时的类型提升，运算后会产生进位，二进制结果为1,11111110，由于进位后超过了byte类型的存储空间，所以进位部分被舍弃，"
                + "即最终的结果为11111110，也就是-2，这正好利用溢位的方式实现了负数的运算。-128在内存中对应的二进制数据为10000000，如果两个-128相加，不考虑Java运算时的类型提升，运算后会产生进位，"
                + "二进制结果为1,00000000，由于进位后超过了byte类型的存储空间，所以进位部分被舍弃，即最终的结果为00000000，也就是0，这样的结果显然不是我们期望的，这说明计算机中的算术运算是会发生越界情况的，"
                + "两个数值的运算结果不能超过计算机中的该类型的数值范围。由于Java中涉及表达式运算时的类型自动提升，我们无法用byte类型来做演示这种问题和现象的实验，大家可以用下面一个使用整数做实验的例子程序体验一下";
        
        FileChannel fc = new FileOutputStream(path).getChannel();
        fc.write(ByteBuffer.wrap(context.getBytes()));
        fc.close();

        fc = new RandomAccessFile(path, "rw").getChannel();
        fc.position(fc.size());
        fc.write(ByteBuffer.wrap(context.getBytes()));
        fc.close();
        
        fc = new FileInputStream(path).getChannel();
        ByteBuffer buff = ByteBuffer.allocate(128); // 分配BSIZE的“卡车”
        int len = 0;
        while((len = fc.read(buff)) != -1){
            //fc.read(buff); // 从“通道”向卡车中装载数据
            System.out.println("读到字节："+len+"个");
            buff.flip(); // 准备buff对象以供write()方法写数据
            //buff.get();//获取buff指定位置字节，同时
            System.out.println(new String(buff.array()));
        }
        fc.close();
    }
    
    public void select(){
        try {
            Selector selector = SelectorProvider.provider().openSelector();
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    // 使用channel复制文件
    @Test
    public void test2(){

        String path1 = "G:/study_test/base/nio/clipboard.png";
        String path2 = "G:/study_test/base/nio/clipboard1.png";
        FileChannel inChannel = null;
        FileChannel outChannel =null;
        
        try {
            inChannel = FileChannel.open(Paths.get(path1), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get(path2), StandardOpenOption.CREATE,StandardOpenOption.WRITE);
            
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(inChannel.read(buffer) != -1){
                buffer.flip();
                outChannel.write(buffer);
                buffer.clear();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inChannel != null){
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outChannel != null){
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    
    }
}
