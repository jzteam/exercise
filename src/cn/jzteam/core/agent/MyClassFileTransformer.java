package cn.jzteam.core.agent;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * 类转换器
 */
public class MyClassFileTransformer implements ClassFileTransformer {
    private final static String allowRetransformerClassNames = "UserInfoInnerController, UserKycInfoCommonServiceImpl";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        try {
            if (className == null || "".equals(className) || !allowRetransformerClassNames.contains(className)) {
                return null;
            }
            System.out.println("开始 MyClassFileTransformer 转换了，className=" + className);
            String currentClassName = className.replaceAll("/", ".");
            CtClass currentClass = ClassPool.getDefault().get(currentClassName);
            CtBehavior[] methods = currentClass.getMethods();
            for (CtBehavior method : methods) {
                // 只给 baseInfo 这个方法加一个日志
                CtClass[] paramsType = method.getParameterTypes();
                for (CtClass type : paramsType) {
                    String typeName = type.getName();
                    System.out.print("param type:" + typeName);
                    if ((long.class.getName().replaceAll("/", ".")).equals(typeName)) {
                        System.out.println(" this is correct ");

                        // 静态类进行设置编码 log.info 的参数不能正常识别，无法打印入参（$1表示第一个入参的值），所以给了一个字符串
                        method.insertAt(0, " log.info(\"这是MyClassFileTransformer植入的日志\");");
                        break;
                    }
                }
                //finish method
            }
            return currentClass.toBytecode();

        } catch (Exception ex) {
            System.out.println("MyClassFileTransformer 报错了，className=" + className);
            ex.printStackTrace();
        }
        System.out.println("结束 MyClassFileTransformer ，className=" + className);
        return null;
    }
}
