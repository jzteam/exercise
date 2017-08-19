package cn.jzteam.server.tomcat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {

    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket cli = new Socket("127.0.0.1", 8888);

        System.out.println("开始写入请求数据...");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(cli.getOutputStream()));
        bw.write("test");

        bw.newLine();
        bw.flush();
        System.out.println("请求数据写入完毕");

        BufferedReader br = new BufferedReader(new InputStreamReader(cli.getInputStream()));
        String line = null;
        while ((line = br.readLine()) != null)
            System.out.println(line);

        bw.close();
        br.close();
    }
}
