package cn.jzteam.core.grammar;

interface A {
    public void demo();
}

interface B {
    public void demo();
}

public class C implements A,B{
    public static void main(String[] args) {
        A a = new C();
        B b = new C();
        a.demo();
        b.demo();
    }
    @Override
    public void demo() {
        System.out.println("打印C");
//        return 0;
    }
}

class Parent
{
    
    public void init()
    {
        System.out.println("1 init parent");
        this.demo();
    }
    public void demo()
    {
        System.out.println("2 demo parent");
    }
}
class Son extends Parent
{
    
    public void init()
    {
        super.init();
        System.out.println("3 init son");
    }
    public void demo()
    {
        System.out.println("4 demo son");
    }
    public static void main(String[] args)
    {
        Son son = new Son();
        son.init();
    
    }
}

class Block
{
    {   //构造块
        System.out.println("1、构造块 ");
    }
    static //静态代码块
    {
        System.out.println("0、静态代码块 ");
    }
    public Block()
    {
        System.out.println("2、构造方法");
    }
    public static void main(String[] args)
    {
        new Block();
        new Block();
    }
}