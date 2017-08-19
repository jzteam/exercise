package cn.jzteam.core.lambda;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

/**
 * lambda表达式，实则是：
 * Thread thread = new Thread(() -> System.out.println("lambda 真棒"));
 * ① 当前类多一个动态调用方法，返回一个CallSite，该CallSite对象包含一个对run无参方法的调用，该方法返回值是Runnable类型
 * 
 * ② 在执行lambda表达式字节码指令时，就是获得CallSite对象后执行run方法，获得一个Runnable对象（对象实际类型cn.jzteam.base.lambda.SimpleTest$$Lambda$1/1304836502）
 * 
 * ③ 当前类还新增一个static方法cn/jzteam/base/lambda/SimpleTest.lambda$0，该方法的代码就是 System.out.println("lambda 真棒")
 * 
 * ④ 在 SimpleTest$$Lambda$1/1304836502 类中的run方法的code字节码指令是对 cn/jzteam/base/lambda/SimpleTest.lambda$0 的调用
 * 
 * 
 * @author Administrator
 *
 */
public class SimpleTest {
    
    public static void main(String[] args) throws Throwable {
        
        SimpleTest simpleTest = new SimpleTest();
        
//        boolean rank = simpleTest.rank((x, y)->{
//            if(x>y){
//                return true;
//            }else{
//                return false;
//            }
//        },12,10);
//        
//        System.out.println(rank);
//         MethodHandles.Lookup caller 
//         String invokedName, 接口中的方法名（本类实现了一个名为接口方法名的方法，返回一个接口类型的对象。该对象对于接口的实现却是对一个static的MethodHandle的调用）
//         MethodType invokedType, 接口类型
        
//         MethodType samMethodType, 接口中方法的返回值类型、参数类型
//         MethodHandle implMethod,  实现方法的MethodHandle，其实是在本来中一个private static的方法
//         MethodType instantiatedMethodType  实现方法的返回值类型和参数类型
        MethodHandles.Lookup caller = MethodHandles.lookup();
        MethodType invokedType = MethodType.methodType(SimpleInterface.class);
        MethodType methodType1 = MethodType.methodType(boolean.class,Object.class,Object.class);
        MethodType methodType2 = MethodType.methodType(boolean.class,Integer.class,Integer.class);
        MethodHandle implMethod = caller.findStatic(SimpleTest.class, "nice", methodType2);
        CallSite cs = LambdaMetafactory.metafactory(caller, "compare", invokedType, methodType1, implMethod, methodType2);
        CallSite cs1 = LambdaMetafactory.metafactory(caller, "compare", invokedType, methodType1, implMethod, methodType2);
        
        System.out.println("cs == cs1:"+(cs == cs1));
        
        MethodHandle factory = cs.getTarget();
        System.out.println("factory="+factory.toString());
        System.out.println("implMethod="+implMethod.toString());
        
        Object obj = factory.invoke();
        
      Class<? extends Object> clazz = obj.getClass();
      String rname = clazz.getName();
      System.out.println("name="+rname);
      
      System.out.println("------interfaces----------");
      Class<?>[] interfaces = clazz.getInterfaces();
      Arrays.asList(interfaces).forEach((x)->System.out.println(x.getName()));
      
      System.out.println("----------methods-------------");
      Arrays.asList(clazz.getMethods()).forEach((x)->{
              System.out.print(x.getReturnType().getName());
              System.out.print("  ");
              System.out.print(x.getName());
              System.out.print("  ");
              System.out.println(x.getParameterTypes());
          });
      


      System.out.println("---------fields-------");
      Arrays.asList(clazz.getFields()).forEach((x)->{
          System.out.print(x.getType().getName()+"  ");
          System.out.print(x.getName());
      });
        
        
        
      SimpleInterface test = (SimpleInterface)obj;
        boolean compare = test.compare(12, 14);
        System.out.println("compare="+compare);
        
//        MethodHandles.Lookup caller = MethodHandles.lookup();
//        MethodType methodType = MethodType.methodType(void.class);
//        MethodType invokedType = MethodType.methodType(Runnable.class);
//        
//        //MethodHandle find = caller.findStatic(SimpleTest.class, "print", methodType);
//        MethodHandle find = caller.findVirtual(SimpleTest.class, "print", methodType).bindTo(simpleTest);
//        find.invoke();
//        CallSite site = LambdaMetafactory.metafactory(caller,   "run",invokedType, methodType, find, methodType);
//        MethodHandle factory = site.getTarget();
//        Object obj = factory.invoke();
//        Class<? extends Object> clazz = obj.getClass();
//        String rname = clazz.getName();
//        System.out.println("name="+rname);
//        
//        System.out.println("------interfaces----------");
//        Class<?>[] interfaces = clazz.getInterfaces();
//        Arrays.asList(interfaces).forEach((x)->System.out.println(x.getName()));
//        
//        System.out.println("----------methods-------------");
//        Arrays.asList(clazz.getMethods()).forEach((x)->{
//                System.out.print(x.getReturnType().getName());
//                System.out.print("  ");
//                System.out.print(x.getName());
//                System.out.print("  ");
//                System.out.println(x.getParameterTypes());
//            });
//        
//
//
//        System.out.println("---------fields-------");
//        Arrays.asList(clazz.getFields()).forEach((x)->{
//            System.out.print(x.getType().getName()+"  ");
//            System.out.print(x.getName());
//        });
//        
//        Runnable r = (Runnable)obj;
//        r.run();
    }
    
    public boolean rank(SimpleInterface<Integer> instance,int x,int y){
        return instance.compare(x, y);
    }
    
    public static boolean nice(Integer x,Integer y){
        if(x>y){
            return true;
        }else{
            return false;
        }
    }
    
    private static boolean come(String str){
        System.out.println("come执行，str="+str);
        return true;
    }
    
    private static void print() {
        System.out.println("hello world"); 
    }

}
