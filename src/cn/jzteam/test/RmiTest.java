package cn.jzteam.test;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import cn.jzteam.zkrmi.impl.HelloServiceImpl;

public class RmiTest {

    public static void main(String[] args) throws Exception {
        int port = 1099;
        String url = "rmi://localhost:1099/cn.jzteam.zkrmi.HelloServiceImpl";
        LocateRegistry.createRegistry(port);
        Naming.rebind(url, new HelloServiceImpl());
    }

}
