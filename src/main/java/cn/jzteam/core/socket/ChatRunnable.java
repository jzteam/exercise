package cn.jzteam.core.socket;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ChatRunnable implements Runnable {
	private Map<String, Socket> map;
	private Socket socket;
	public ChatRunnable(Socket socket, Map<String, Socket> map) {
		this.socket = socket;
		this.map = map;
	}
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				Set<Entry<String, Socket>> entrySet = map.entrySet();
				for (Entry<String, Socket> entry : entrySet) {
					System.out.println("�յ�" + entry.getKey() + "������:" + line);
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(entry.getValue().getOutputStream(),"utf-8"));
					bw.write(entry.getValue().getInetAddress().getHostName()+"˵��"+line);
					bw.newLine();
					bw.flush();
				}
			}
		} catch (IOException e) {
			System.out.println(socket.getInetAddress().getHostName()+"�˳�");
		}
	}
}
