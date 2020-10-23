package cn.jzteam.core.agent;

import com.okcoin.ucenter.users.service.impl.kyc.basic.UserKycInfoCommonServiceImpl;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class FirstJavaAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("FirstJavaAgent premain hhhh agentArgs=" + agentArgs);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("FirstJavaAgent agentmain hhhh agentArgs=" + agentArgs + ", inst=" + inst);
        inst.addTransformer(new MyClassFileTransformer(), true);
        System.out.println("FirstJavaAgent addTransformer");
        inst.retransformClasses(UserKycInfoCommonServiceImpl.class);
        System.out.println("FirstJavaAgent agentmain finish hhhh");
    }
}
