package com.leo.camel.jetty;

import org.apache.camel.DynamicRouter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMessage;
import org.apache.camel.impl.DefaultCamelContext;

import org.apache.camel.model.ModelCamelContext;
import org.apache.http.protocol.HTTP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public static void main(String args[]) throws Exception {
        ModelCamelContext context = new DefaultCamelContext();


        RouteBuilder route1 = new RouteBuilder() {
            public void configure() throws Exception {
                from("jetty:http://0.0.0.0:7070/myservice/1/{}")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {

                                HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);

                                System.out.println("getMethod: " + request.getMethod());
                                System.out.println("getContextPath: " + request.getContextPath());
                                System.out.println("getQueryString: " + request.getQueryString());
                                System.out.println("getRequestURI:" + request.getRequestURI());

                                exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant("username=zhangsan&password=123"));
                            }
                        })
                        .to("jetty:http://localhost:8080/camel_test/camel/camelService");

            }
        };


        RouteBuilder route2 = new RouteBuilder() {
            public void configure() throws Exception {
                from("jetty:http://0.0.0.0:7070/myservice/2/{camel}/{camelService}")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {

                                HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();

                                System.out.println("getMethod: " + request.getMethod());
                                System.out.println("getContextPath: " + request.getContextPath());
                                System.out.println("getQueryString: " + request.getQueryString());
                                System.out.println("getRequestURI:" + request.getRequestURI());

                                //这边的含义是能够输出流,此处可以将post和get方法合并
                                exchange.getOut().setBody(getRequestByGet("http://localhost:8080/camel_test/camel/camelService?username=zhangsan&password=1234"));

                            }
                        });
            }
        };


        RouteBuilder route3 = new RouteBuilder() {
            public void configure() throws Exception {
                from("jetty:http://0.0.0.0:7070/myservice/3/{}/{}/{}")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {

                                HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);

                                System.out.println("getMethod: " + request.getMethod());
                                System.out.println("getContextPath: " + request.getContextPath());
                                System.out.println("getQueryString: " + request.getQueryString());
                                System.out.println("getRequestURI:" + request.getRequestURI());

                                exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant("username=zhangsan&password=123"));
                            }
                        })
                        .to("jetty:http://localhost:8080/camel_test/camel/camelService");

            }
        };


//        context.addRoutes(route1);
        context.addRoutes(route2);
//        context.addRoutes(route3);
        context.start();

    }

    /**
     * 发送http的post请求
     */
    public static BufferedReader getRequestByPost(String url, String params) throws IOException {
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
//        String line;
//        while ((line = rd.readLine()) != null) {
//            data.append(line);
//        }
//        rd.close();
//        return data.toString();
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
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }

}
