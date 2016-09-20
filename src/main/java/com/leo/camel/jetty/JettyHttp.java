package com.leo.camel.jetty;

import jdk.internal.dynalink.beans.StaticClass;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMessage;
import org.apache.camel.impl.DefaultCamelContext;

import org.apache.camel.model.ModelCamelContext;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


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

    static String jettyHost = "127.0.0.1";
    static String jettyPort = "7070";

    static String[] appNames = {"app1", "app2", "app3"};


    public static void main(String args[]) throws Exception {
        ModelCamelContext context = new DefaultCamelContext();

        for (String app : appNames) {

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
                                         * getRequestURI:/3/camle_test/camle/camelService
                                         * realRequestUrI: /camle_test/camle/camelService
                                         * appName: camle_test
                                         */
                                        System.out.println("getMethod: " + request.getMethod());
                                        System.out.println("getRequestURI:" + request.getRequestURI());
                                        String realRequestUrI = request.getRequestURI().substring(2);
                                        String appName = realRequestUrI.split("/")[1];

                                        System.out.println("realRequestUrI: " + realRequestUrI);
                                        System.out.println("appName: " + appName);
                                        if ("GET".equals(request.getMethod())) {
                                            //直接将流输出
                                            System.out.println("post请求参数: " + request.getQueryString());
                                            //根据appName可以获取应用的访问路径
                                            String requestUrl = "http://localhost:8080/camel_test/camel/camelService?" + request.getQueryString();
                                            exchange.getOut().setBody(getRequestByGet(requestUrl));
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


//        RouteBuilder route2 = new RouteBuilder() {
//            public void configure() throws Exception {
//                from("jetty:http://0.0.0.0:7070/myservice/2/{camel}/{camelService}")
//                        .process(new Processor() {
//                            @Override
//                            public void process(Exchange exchange) throws Exception {
//
//                                HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
//
//                                System.out.println("getMethod: " + request.getMethod());
//                                System.out.println("getContextPath: " + request.getContextPath());
//
//                                System.out.println("getRequestURI:" + request.getRequestURI());
//                                System.out.println("测试: "+"GET".equals(request.getMethod()));
//                                if ("GET".equals(request.getMethod())){
//                                    //直接将流输出
//                                    System.out.println("getQueryString: " + request.getQueryString());
//                                    String requestUrl = "http://localhost:8080/camel_test/camel/camelService?"+request.getQueryString();
//                                    System.out.println("requestUrl: "+requestUrl);
//                                    exchange.getOut().setBody(getRequestByGet(requestUrl));
//                                }else{
//
//                                    System.out.println("getQueryString: " + request.getQueryString());
//                                    exchange.getOut().setBody(getRequestByPost("http://localhost:8080/camel_test/camel/camelService", "username=zhangsan&password=1234"));
//                                }
//                            }
//                        });
//            }
//        };


//        context.addRoutes(route1);
        //context.addRoutes(route2);
//        context.addRoutes(route3);
        context.start();

    }

    /**
     * 发送http的post请求
     */
    public static BufferedReader getRequestByPost(String url, String params) throws IOException {

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
        return rd;
    }


    /**
     * 发送http的get请求
     */
    public static BufferedReader getRequestByGet(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(false);
        conn.setDoInput(true);

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        return rd;
    }

}
