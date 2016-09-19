/***
 * *author Administrator
 * *date 2016/9/7
 ***/
package com.leo.camel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMessage;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;

import javax.servlet.http.HttpServletRequest;

import static oracle.sql.DatumWithConnection.assertNotNull;
import static org.apache.camel.component.dataset.DataSetEndpoint.assertEquals;

/**
 * 郑重其事的写下 helloworld for Apache Camel
 * @author yinwenjie
 */
public class HelloWorld extends RouteBuilder {
    public static void main(String[] args) throws Exception {
        // 这是camel上下文对象，整个路由的驱动全靠它了。
        ModelCamelContext camelContext = new DefaultCamelContext();
        // 启动route
        camelContext.start();
        // 将我们编排的一个完整消息路由过程，加入到上下文中
        camelContext.addRoutes(new HelloWorld());

        /*
         * ==========================
         * 为什么我们先启动一个Camel服务
         * 再使用addRoutes添加编排好的路由呢？
         * 这是为了告诉各位读者，Apache Camel支持动态加载/卸载编排的路由
         * 这很重要，因为后续设计的Broker需要依赖这种能力
         * ==========================
         * */

        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
//        synchronized (HelloWorld.class) {
//            HelloWorld.class.wait();
//        }
    }

    @Override
    public void configure() throws Exception {
        // 在本代码段之下随后的说明中，会详细说明这个构造的含义





        from("jetty:http://0.0.0.0:8282/myapp/myservice")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println(exchange.getOut().toString());

                        //System.out.println(exchange.getContext().toString());

                    }
                })
                .to("jetty:http://localhost:8080/camel_test/camleTestServlet?username=zhangsan&password=123");


//        from("jetty:http://0.0.0.0:8282/doHelloWorld")
//                .process(new MyBookService())
//                .to("log:helloworld?showExchangeId=true");
    }

    /**
     * 这个处理器用来完成输入的json格式的转换
     * @author yinwenjie
     */
    public class MyBookService implements Processor {
        public void process(Exchange exchange) throws Exception {
//            // just get the body as a string
//            String contentType = exchange.getIn().getHeader(Exchange.CONTENT_TYPE, String.class);
//            String path = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
//            path = path.substring(path.lastIndexOf("/"));
//
//            // we have access to the HttpServletRequest here and we can grab it if we need it
//            HttpServletRequest req = exchange.getIn().getBody(HttpServletRequest.class);
//               // send a html response
//            exchange.getOut().setHeader(Exchange.CONTENT_TYPE, contentType + "; charset=UTF-8");
//            exchange.getOut().setHeader("PATH", path);
            exchange.getOut().setBody("<b>Hello World</b>");
        }


    }
}