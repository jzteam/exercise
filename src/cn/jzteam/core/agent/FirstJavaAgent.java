package cn.jzteam.core.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class FirstJavaAgent {
    /**
     * META-INF/MANIFEST.MF 中需要指定：Premain-Class
     * 使用时在启动参数中增加 -javaagent=xxx.jar
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("FirstJavaAgent premain hhhh agentArgs=" + agentArgs);
    }

    /**
     * META-INF/MANIFEST.MF 中需要指定：Agent-Class
     * 使用时通过另外进程控制目标程序加载新转换器 VirtualMachine.attach(pid).attach(xxx.jar);
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("FirstJavaAgent agentmain test3 agentArgs=" + agentArgs + ", 开始添加转换器");

        MyClassFileTransformer myClassFileTransformer = new MyClassFileTransformer();

        inst.addTransformer(myClassFileTransformer, true);

        try {
            Class[] allLoadedClasses = inst.getAllLoadedClasses();
            System.out.println("FirstJavaAgent getAllLoadedClasses finish，size=" + allLoadedClasses.length);
            for (Class clazz : allLoadedClasses) {
                // 这里使用getSimpleName会导致重新类加载，也会执行新的Transformer接口
                // 可能遇到有些jar中的类找不到class文件无法重新加载的情况，如 SpringCoreBlockHoundIntegration implements BlockHoundIntegration，找不到 BlockHoundIntegration
                // if (clazz.getSimpleName().equalsIgnoreCase("UserInfoInnerController")) {
                    // 会导致重新转换所有类，虽然新的tranformer会过滤掉，但是底层收集类信息或者应用其他tranformer时，会报错部分类找不到
                    // inst.retransformClasses(clazz);
                // }
            }
        } finally {
            // 为了方便测试，重复添加转换器，所以用完了就移除转换器
            inst.removeTransformer(myClassFileTransformer);
            System.out.println("FirstJavaAgent 移除了转换器");
        }
        System.out.println("FirstJavaAgent agentmain finish test3");
    }
}
