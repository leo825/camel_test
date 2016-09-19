/***
 * *author Administrator
 * *date 2016/9/13
 ***/
package com.leo.camel;


import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2016/9/13.
 */
public class HttpPoxy {

    public static void main(String[] agrs) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpHost target = new HttpHost("localhost", 8080, "http");
            HttpHost proxy = new HttpHost("127.0.0.1", 8080, "http");

            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();
            HttpGet request = new HttpGet("http://127.0.0.1:8080/camel_test/camelClient");
            request.setConfig(config);

            System.out.println("Executing request " + request.getRequestLine() + " to " + target + " via " + proxy);

            CloseableHttpResponse response = httpclient.execute(target, request);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } finally {
            //httpclient.close();
        }
    }
}
