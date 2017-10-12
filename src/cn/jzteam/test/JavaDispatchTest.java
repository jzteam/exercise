package cn.jzteam.test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class JavaDispatchTest {

    static class Human {
        public void say() throws Throwable {
            System.out.println("human...");
        }
        public void eat(){
            System.out.println("human11111 eat...");
        }
    }

    static class Man extends Human {
        public void say() throws Throwable {
            
            System.out.println("man...");
        }
        
        public void eat(){
            System.out.println("man222222 eat...");
        }
    }
    
    static class Person extends Man {
        public void say() throws Throwable {
            MethodType methodType = MethodType.methodType(void.class);
            MethodHandle findSpecail = MethodHandles.lookup().findSpecial(Human.class, "eat", methodType,getClass());
            findSpecail.invoke(this);
            System.out.println("Person...");
        }
        public void eat(){
            System.out.println("Person33333 eat...");
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

    public static void main(String[] args) throws Throwable {
//        JavaDispatchTest dispatchTest = new JavaDispatchTest();
//        Human man = new Man();
//        Human woman = new Woman();
//        dispatchTest.say(man);
//        dispatchTest.say(woman);
        
        MethodType methodType = MethodType.methodType(void.class);
//        MethodHandle findVirtual = MethodHandles.lookup().findVirtual(Human.class, "say", methodType);
//        findVirtual.invoke(new Person());
        
        // no private access for invokespecial: class cn.jzteam.test.JavaDispatchTest$Human, from cn.jzteam.test.JavaDispatchTest
        // findSpecial的调用，必须在
//        MethodHandle findSpecail = MethodHandles.lookup().findSpecial(Human.class, "eat", methodType,Person.class).bindTo(new Person());
//        findSpecail.invoke();
        
        new Person().say();
    }

}
