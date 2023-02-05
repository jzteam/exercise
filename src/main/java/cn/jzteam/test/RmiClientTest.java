package cn.jzteam.test;

import java.rmi.Naming;

import cn.jzteam.core.rmi.HelloService;

public class RmiClientTest {

    public static void main(String[] args) throws Exception {
        String url = "rmi://localhost:1099/cn.jzteam.zkrmi.HelloServiceImpl";
        HelloService helloService = (HelloService) Naming.lookup(url);
        String say = helloService.say("我要疯掉了");
        System.out.println("客户端：" + say);
    }

}
