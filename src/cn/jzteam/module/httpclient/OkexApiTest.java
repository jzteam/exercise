package cn.jzteam.module.httpclient;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.security.MessageDigest;
import java.util.*;

public class OkexApiTest {

    public static void main(String[] args) throws Exception{
        String apiKey = "097a40e6-3a70-40ef-8390-e87233127045";
        String secretKey = "FDC07EEAE83CADEE2B43E0C3AABCFDFF";

//        doGet(new HashMap<>());
        doPost(new HashMap<>(),"https://www.okex.com/api/v1/userinfo.do");
    }

    public static void doPost(Map<String,String> params,String url) throws Exception{

        String apiKey = "097a40e6-3a70-40ef-8390-e87233127045";
        String secretKey = "FDC07EEAE83CADEE2B43E0C3AABCFDFF";

        if(url == null || url.trim().equals("")){
            return;
        }

        params.put("api_key",apiKey);

        try(CloseableHttpClient httpclient = HttpClients.createDefault()){

            HttpPost httpPost = new HttpPost(url);

            List <NameValuePair> nvps = new ArrayList <>();
            Set<String> keys = params.keySet();
            for(String key : keys){
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
            nvps.add(new BasicNameValuePair("sign", md5Signature(params,secretKey)));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try(CloseableHttpResponse response = httpclient.execute(httpPost)) {
                int status = response.getCode();
                if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                    HttpEntity entity = response.getEntity();
                    System.out.println(EntityUtils.toString(entity));
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        }
    }

    public static void doGet(Map<String,String> params) throws Exception{
        try(CloseableHttpClient httpclient = HttpClients.createDefault()){

            HttpGet httpGet = new HttpGet("https://www.okex.com/api/v1/exchange_rate.do");

            try(CloseableHttpResponse response = httpclient.execute(httpGet)) {
                int status = response.getCode();
                if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                    HttpEntity entity = response.getEntity();
                    System.out.println(EntityUtils.toString(entity));
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        }
    }

    public static String md5Signature(Map<String, String> params, String secret) {
        String result = null;
        String origin = getBeforeSign(params);
        if (origin == null) {
            return result;
        }

        origin += "&secret_key="+secret;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byte2hex(md.digest(origin.getBytes("utf-8")));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("sign error !");
        }
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String tempStr = "";

        for(int i = 0; i < b.length; ++i) {
            tempStr = Integer.toHexString(b[i] & 255);
            if (tempStr.length() == 1) {
                hs.append("0").append(tempStr);
            } else {
                hs.append(tempStr);
            }
        }

        return hs.toString().toUpperCase();
    }

    private static String getBeforeSign(Map<String, String> params) {
        if (params == null) {
            return null;
        } else {
            Map<String, String> treeMap = new TreeMap();
            treeMap.putAll(params);
            Iterator iterator = treeMap.keySet().iterator();

            StringBuilder sb = new StringBuilder();
            while(iterator.hasNext()) {
                String name = (String)iterator.next();
                sb.append(name).append("=").append((String)params.get(name)).append("&");
            }

            return sb.substring(0,sb.length()-1);
        }
    }
}
