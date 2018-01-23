package cn.jzteam.work;

public class TestAutowired {

    private static String test;

    public String getTest(){
        return test;
    }

    public TestAutowired(String test){
        this.test = test;
    }

    public static void main(String[] args) {
        final TestAutowired we = new TestAutowired("we");
        System.out.println(we.getTest());

    }
}
