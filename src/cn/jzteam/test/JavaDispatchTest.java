package cn.jzteam.test;

public class JavaDispatchTest {

    static class Human {
        public void say() {
            System.out.println("human...");
        }
    }

    static class Man extends Human {
        public void say() {
            System.out.println("man...");
        }
    }

    static class Woman extends Human {
        public void say() {
            System.out.println("woman...");
        }
    }

    public void say(Human human) {
        System.out.println("Human human...");
        // human.say();
    }

    public void say(Man man) {
        System.out.println("Man man...");
    }

    public void dispatch(char a) {
        System.out.println("char..." + a);
    }

    public void dispatch(Character a) {
        System.out.println("Character..." + a.toString());
    }

    public void dispatch(Object a) {
        System.out.println("object..." + a);
    }

    public static void main(String[] args) {
        JavaDispatchTest dispatchTest = new JavaDispatchTest();
        Human man = new Man();
        Human woman = new Woman();
        dispatchTest.say(man);
        dispatchTest.say(woman);

    }

}
