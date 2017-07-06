package cn.jzteam.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class DispatchTest {
    
    static class GrandFather{
        public void say(char c){
            System.out.println("grandfather say char");
        }
    }
    
    static class Father extends GrandFather{
        public void say(char c){
            System.out.println("father say char");
        }
        
    }
    
    static class Son{
        
        public void say(Character c){
            System.out.println("son say Character");
        }
        public void say(char c){
            System.out.println("son say int");
            try{
                // MethodType mt = MethodType.fromMethodDescriptorString("(C;)V;", null); //空指针，不知为什么
                MethodType mt = MethodType.methodType(void.class, char.class);
                
                // 使用 findSpecial就是用一个类型的对象去调用另一个类型的对象的方法，继承关系还是必须要有的
                MethodHandle mh = MethodHandles.lookup().findSpecial(GrandFather.class, "say", mt,this.getClass());
                
                // 使用findVirtual分派规则就是使用实际类型来调用了，死循环son.say方法
                // MethodHandle mh = MethodHandles.lookup().findVirtual(GrandFather.class, "say", mt).bindTo(this);
                // mh.invoke('c');
                
                mh.invoke(this,'c');
            }catch(Throwable e){
                System.out.println("演示失败");
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        Son son = new Son();
        son.say('c');
        
    }

}
