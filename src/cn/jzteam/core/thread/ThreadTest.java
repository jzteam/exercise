package cn.jzteam.core.thread;

public class ThreadTest {

    static class MyTask implements Runnable{
        private Object lockA = null;
        private Object lockB = null;
        private String name = null;

        public MyTask(Object lock1,Object lock2,String name) {
            this.lockA = lock1;
            this.lockB = lock2;
            this.name = name;
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            for(int i=0;i<10;i++) {
                synchronized(lockA) {

                    try {
                        synchronized (lockB) {
                            System.out.println(name+" is running "+i);
                            lockB.notify();
                        }
                        lockA.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    public static void main(String[] args) {
        // 有序执行
        Object lock1 = new Object();
        Object lock2 = new Object();
        Object lock3 = new Object();
        new Thread(new MyTask(lock1,lock3,"A")).start();
        new Thread(new MyTask(lock2,lock1,"B")).start();
        new Thread(new MyTask(lock3,lock2,"C")).start();

    }
}