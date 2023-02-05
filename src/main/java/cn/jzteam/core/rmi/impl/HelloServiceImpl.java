package cn.jzteam.core.rmi.impl;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

import cn.jzteam.core.rmi.HelloService;


public class HelloServiceImpl extends UnicastRemoteObject implements HelloService{
	private static final long serialVersionUID = 1L;

	public HelloServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public String say(String name) throws RemoteException {
		System.out.println("服务端输出："+name);
		String clientHost = null;
		try {
			clientHost = RemoteServer.getClientHost();
		} catch (ServerNotActiveException e) {
		}
		return name+"  getClientHost:"+clientHost;
	}

}