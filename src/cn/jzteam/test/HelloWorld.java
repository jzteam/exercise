package cn.jzteam.test;

import com.sun.tools.attach.VirtualMachine;

import java.io.*;

public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("hello world!");
        try {
            // jps 查到tomcat的进程号
            // 不能执行 jps | grep UsersApplication | cut -d ' ' -f1，只好手动解析
            Process process = Runtime.getRuntime().exec("jps");
            // 阻塞直到命令完成得到结果
            int exitValue = process.waitFor();
            if(exitValue != 0) {
                System.out.println("获取进程号失败 exitValue=" + exitValue);
                BufferedReader errorBr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                System.out.println("error： " + errorBr.readLine());
                return;
            }
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String result = null;
            while ((result = br.readLine()) != null) {
                System.out.println("result="+result);
                if (result.contains("UsersApplication")) {
                    result = result.substring(0, result.indexOf(" "));
                    break;
                }
            }
            System.out.println("UsersApplication 的进程号是 " + result);
            if (result != null && result != "") {
                VirtualMachine attach = VirtualMachine.attach(result);
                attach.loadAgent("/Users/jzteam/code/test/firstJavaAgent.jar");
                System.out.println("attach finish");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
