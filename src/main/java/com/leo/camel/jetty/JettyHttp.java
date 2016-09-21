package com.leo.camel.jetty;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMessage;
import org.apache.camel.impl.DefaultCamelContext;

import org.apache.camel.model.ModelCamelContext;
import sun.awt.image.PNGImageDecoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Administrator on 2016/9/7.
 */


/**
 * 需要解决的问题：
 * <p/>
 * 1.如何处理post请求
 * 2.如何处理路径问题
 */
public class JettyHttp {

    static String jettyHost = "0.0.0.0";
    static String jettyPort = "9090";

    static String[] appServerNames = {"app1", "app2", "app3"};

    static String[] mapAppServerNames = {"PGIS_S_TileMapServer", "PGIS_S_TileMapServer2"};




    public static void main(String args[]) throws Exception {
        ModelCamelContext context = new DefaultCamelContext();

        for (String app : appServerNames) {

            final String appName = app;
            for (int i = 1; i <= 5; i++) {
                final int contextPage = i;
                RouteBuilder route = new RouteBuilder() {

                    public void configure() throws Exception {

                        String jettyURL = "http://" + jettyHost + ":" + jettyPort + "/" + appName;
                        for (int j = 1; j <= contextPage; j++) {
                            jettyURL += "/{}";
                        }

                        System.out.println(jettyURL);

                        from("jetty:" + jettyURL)
                                .process(new Processor() {
                                    @Override
                                    public void process(Exchange exchange) throws Exception {

                                        HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();

                                        /**
                                         * 举例子：
                                         * getRequestURI: /app1/PGIS_S_TileMapServer/Maps/vec_tj/EzMap
                                         * appName: app1
                                         */
                                        System.out.println("getMethod: " + request.getMethod());
                                        System.out.println("getRequestURI:" + request.getRequestURI());
                                        String realRequestUrI = request.getRequestURI();//此处有问题，等待修改
                                        String appName = realRequestUrI.split("/")[1];

                                        System.out.println("appName: " + appName);
                                        if ("GET".equals(request.getMethod())) {
                                            //直接将流输出
                                            System.out.println("get请求参数: " + request.getQueryString());
                                            //根据appName可以获取应用的访问路径
                                            String requestUrl = "http://192.168.0.101/PGIS_S_TileMapServer/Maps/vec_tj/EzMap?" + request.getQueryString();

                                            if (Arrays.asList(mapAppServerNames).contains("PGIS_S_TileMapServer")) {
                                                exchange.getOut().setBody("<html><body><img src=\"http://192.168.0.101/PGIS_S_TileMapServer/Maps/vec_tj/EzMap?Service=getImage&amp;Type=RGB&amp;Col=6&amp;Row=3&amp;Zoom=5&amp;V=0.3\"></body></html>");
                                            } else {
                                                exchange.getOut().setBody(getRequestByGet(requestUrl));
                                            }

                                        } else {
                                            String params = exchange.getIn().getBody(String.class);
                                            System.out.println("post请求参数" + params);
                                            String requestUrl = "http://localhost:8080/camel_test/camel/camelService";
                                            exchange.getOut().setBody(getRequestByPost(requestUrl, params));
                                        }
                                    }
                                });
                    }
                };

                context.addRoutes(route);
            }
        }

        context.start();

    }

    /**
     * 发送http的post请求
     */
    public static String getRequestByPost(String url, String params) throws IOException {

        System.out.println("params:" + params);
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoOutput(true);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(params);
        out.flush();
        out.close();

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }


    /**
     * 发送http的get请求
     */
    public static String getRequestByGet(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(false);
        conn.setDoInput(true);

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }

}
