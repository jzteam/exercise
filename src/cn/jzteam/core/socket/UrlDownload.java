package cn.jzteam.core.socket;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class UrlDownload {

	public static void main(String[] args) throws Exception {

		URL url = new URL("http://www.xiamp4.com/Html/GP23198.html");
		InetAddress inet = InetAddress.getByName("www.xiamp4.com");
		if (!inet.isReachable(10000)) {
			System.out.println("���Ե���");
			return;
		}
		String host = url.getHost();
		System.out.println("host:" + host);
		URLConnection conn = url.openConnection();
		String encoding = conn.getContentEncoding();
		System.out.println("encoding:"+encoding);
		int length = conn.getContentLength();
		System.out.println("length:" + length);
		
		InputStream is = conn.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "gbk"));
		String line = null;
		while((line = br.readLine())!=null){
			System.out.println(line);
		}

	}

}
