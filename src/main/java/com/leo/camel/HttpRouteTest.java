/***
 * *author Administrator
 * *date 2016/9/7
 ***/
package com.leo.camel;

import org.apache.camel.DynamicRouter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import org.apache.camel.model.ModelCamelContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Administrator on 2016/9/7.
 */

public class HttpRouteTest {

    public static void main(String args[]) throws Exception {
        ModelCamelContext context = new DefaultCamelContext();


        RouteBuilder route = new RouteBuilder() {
            public void configure() throws Exception {
                from("jetty:http://0.0.0.0:7070/myservice")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                System.out.println(exchange.getOut().toString());
                                System.out.println(exchange.getOut().getHeaders().toString());
                                System.out.println(exchange.getContext().toString());
                                exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant("username=zhangsan&password=123"));
                            }
                        })
                        .to("jetty:http://localhost:8080/camel_test/camel/camelService");

            }
        };

        context.addRoutes(route);
        context.start();

    }
}
