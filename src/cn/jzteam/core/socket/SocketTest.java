package cn.jzteam.core.socket;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

public class SocketTest {

	@Test
	public void test1() throws Exception {
		// Inet4Address
		InetAddress inetAddress = Inet4Address.getByName("www.61yzt.com");
		// �����Ƿ�ɵ���
		boolean reachable = inetAddress.isReachable(10000);
		System.out.println("�Ƿ�ﵽ��" + reachable);
		// ��ȡip
		String ip = inetAddress.getHostAddress();
		// ��ȡ������
		String name = inetAddress.getHostName();
		// ��ȡȨ�޶�����
		String canonicalName = inetAddress.getCanonicalHostName();
		System.out.println("ip:" + ip);
		System.out.println("������:" + name);
		System.out.println("Ȩ�޶�������" + canonicalName);
		// ��������
		System.out.println(InetAddress.getLocalHost());
	}

	/**
	 * openStream���ܶ������е��ֽڣ�why????????
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception{
		URL url = new URL("http://www.61yzt.com/yztapp/dataCenter/html/list?type=0");
		System.out.println("userinfo:"+url.getUserInfo());
		String file = url.getFile();
		System.out.println("ƴ�ӣ�"+url.getProtocol()+"://"+url.getHost()+(url.getPort()==-1?"":":"+url.getPort())+url.getPath()+"?"+url.getQuery());
		System.out.println("file:"+file);
		InputStream is = url.openStream();
		
		byte[] buf = new byte[1024];
		int i = 0;
		int count = 0;
		int len = 0;
		while((len = is.read(buf)) != -1){
			count += len;
			System.out.println(new String(buf,"utf-8"));
			System.out.println("ѭ����"+ ++i+"�Σ�"+len+"�ֽ�");
		}
		System.out.println("��������"+count+"�ֽ�");
	}
	
	@Test
	public void test3() throws Exception{
		URL url = new URL("http://www.61yzt.com/yztapp/dataCenter/html/list?type=0");
		URLConnection conn = url.openConnection();
		System.out.println("���ȣ�"+conn.getContentLength());
		System.out.println("���ͣ�"+conn.getContentType());
	}
	
	@Test
	public void test4() throws Exception{
		URL url = new URL("http://www.61yzt.com/yztapp/dataCenter/html/list");
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Accept-Encoding", "text/plain");
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write("type=0&title=bed".getBytes("utf-8"));
		InputStream is = conn.getInputStream();
		byte[] buf = new byte[1024];
		int i = 0;
		int count = 0;
		int len = 0;
		while((len = is.read(buf)) != -1){
			count += len;
			System.out.println(new String(buf,"utf-8"));
			System.out.println("ѭ����"+ ++i+"�Σ�"+len+"�ֽ�");
		}
		System.out.println("��������"+count+"�ֽ�");
	}
}
