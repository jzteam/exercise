package cn.jzteam.module.httpclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class FirstTest {

    public static void main(String[] args) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet("http://portal.lietou.com/login.jsp");

        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();

        System.out.println("去HPO登录页: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
        System.out.println("处理返回的cookies:");
        List<Cookie> cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println(" - " + cookies.get(i).getName() + "=" + cookies.get(i).getValue());
            }
        }

        HttpPost httpost = new HttpPost("http://portal.lietou.com/login.json");

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("lt_username", "fengpeng"));
        nvps.add(new BasicNameValuePair("lt_password", "test"));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        response = httpclient.execute(httpost);
        entity = response.getEntity();

        System.out.println("登录状态: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }

        System.out.println("Post logon cookies:");
        cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }

    }

}
