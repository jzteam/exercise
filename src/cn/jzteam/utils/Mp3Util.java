package cn.jzteam.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Mp3Util {

    public static void main(String[] args) throws IOException {
//        String src = "C:\\CloudMusic\\宋冬野 - 董小姐.mp3";
//        String desc = "C:\\CloudMusic\\cut.mp3";
        String src = "/Users/jzteam/Documents/work/test_data/test.mov";
        String desc = "/Users/jzteam/Documents/work/test_data/test_small.mov";

        Mp3Util.cutByPath(src, desc, 0, 4, 16);
    }

    public static void cutByPath(String src, String desc, long beginSecond, long endSecond, long totalSecond)
            throws IOException {
        File srcFile = new File(src);
        if (!srcFile.exists()) {
            throw new RuntimeException("源文件不存在！");
        }

        File descFile = new File(desc);
        if (!descFile.exists()) {
            File parentFile = descFile.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                System.out.println("生成目标父目录");
                parentFile.mkdirs();
            }
        }

        double beginRate = (double) beginSecond / totalSecond;
        double endRate = (double) endSecond / totalSecond;

        cut(srcFile, descFile, beginRate, endRate);
        // cutByte(srcFile, descFile, 0, 20000000);

        System.out.println("剪辑完成！");

    }

    public static void cut(File src, File desc, double beginRate, double endRate) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(desc));

        int totalSize = bis.available();
        int beginSize = (int) (totalSize * beginRate);
        int endSize = (int) (totalSize * endRate);
        System.out.println("文件总长：" + totalSize + ",起始字节：" + beginSize + ",结束字节：" + endSize);

        try {
            cutByte(bis, bos, beginSize, endSize);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
            }
            try {
                bos.close();
            } catch (IOException e) {
            }
        }



    }

    public static void cutByte(BufferedInputStream bis, BufferedOutputStream bos, int begin, int end)
            throws IOException {

        System.out.println("字节总量：" + bis.available() + ",起始字节：" + begin + ",结束字节：" + end);

        int left = end - begin;

        // // 读取文件头信息
        // byte[] headBuf = new byte[60];
        // int headLen = 0;
        // if ((headLen = bis.read(headBuf)) > 0) {
        // bos.write(headBuf, 0, headLen);
        // }
        // System.out.println("读取头文件：headLen=" + headLen);

        bis.skip(begin);

        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = bis.read(buf)) != -1 && left > 0) {
            System.out.println("剩余 " + left + " 字节 ,刚读了 " + len + " 个字节");
            if (left > len) {
                bos.write(buf, 0, len);
            } else {
                bos.write(buf, 0, left);
            }
            left -= len;
            System.out.println("读完了，剩余 " + left + " 字节");
        }

        System.out.println("len=" + len + ",left=" + left);


    }



}
