package cn.jzteam.zkrmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloService extends Remote{
	
	String say(String name) throws RemoteException;

}
