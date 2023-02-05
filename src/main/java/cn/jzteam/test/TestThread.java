package cn.jzteam.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThread {

    static class TestRunnable implements Runnable {
        public void run() {
            System.out.println("是这个线程对象完成我的：" + Thread.currentThread().getName() + ",thread="
                    + Thread.currentThread().hashCode());
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (int i = 0; i < stackTrace.length; i++) {
                String log = stackTrace[i].getClassName() + "." + stackTrace[i].getMethodName() + ":"
                        + stackTrace[i].getFileName() + "(line:" + stackTrace[i].getLineNumber() + ")";
                System.out.println(log);
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 1; i++) {
            executorService.submit(new TestRunnable());
        }
    }

}
