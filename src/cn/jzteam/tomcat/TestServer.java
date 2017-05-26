package cn.jzteam.tomcat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8888);

        try {
            while (true) {
                Socket accept = ss.accept();
                handleRequest(accept);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ss.close();
        }
    }

    private static void handleRequest(Socket accept) {
        String response = "HTTP/1.0 200 OK\r\n" + "Content-type: text/plain\r\n" + "\r\n" + "Hello World\r\n";

        System.out.println("收到请求，放入工作队列中：");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            System.out.println("睡眠被打断");
        }
        System.out.println("开始处理请求：");

        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            System.out.println("读取请求数据...");
            byte[] buf = new byte[1024];
            bis = new BufferedInputStream(accept.getInputStream());
            bis.read(buf);
            System.out.println(new String(buf));


            System.out.println();
            System.out.println("写入响应数据...");
            bos = new BufferedOutputStream(accept.getOutputStream());
            bos.write(response.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("不支持UTF-8编码");
        } catch (IOException e) {
            System.out.println("socket操作失败");
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }
        System.out.println("请求处理结束");
    }
}
