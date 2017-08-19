package cn.jzteam.core.socket;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class InputRunnable implements Runnable {
	private Socket socket;

	public InputRunnable(Socket socket) {
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			Scanner scanner = new Scanner(System.in);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
			String line = null;
			while (true) {
				if (scanner.hasNext()) {
					line = scanner.nextLine();
					bw.write(line);
					bw.newLine();
					bw.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
