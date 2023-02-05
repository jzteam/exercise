package cn.jzteam.test;


public class ThreadLocalTest {

    public static void main(String[] args) {


        ThreadLocalTest test = new ThreadLocalTest();

        test.create();

        test.update();

    }

    public void create() {
        System.out.println("create....");
        ThreadLocal<String> local = new ThreadLocal<String>();

        local.set(new String("first test"));

    }

    public void update() {
        System.out.println("update....");
        ThreadLocal<String> local = new ThreadLocal<String>();

        System.out.println("local.get=" + local.get());
    }

}
