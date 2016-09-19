/***
 * *author Administrator
 * *date 2016/9/18
 ***/
package com.leo.camel;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import java.util.List;

import static org.apache.camel.builder.Builder.constant;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ServletRoute {


    @Autowired
    @Qualifier("integrationLayer")
    private CamelContext camelContext;

    public static void main(String args[]) throws Exception {
       ModelCamelContext context = new DefaultCamelContext();


        RouteBuilder route = new RouteBuilder() {
            public void configure() throws Exception {
//                from("direct:start").process(new Processor() {
//                    @Override
//                    public void process(Exchange exchange) throws Exception {
//                        System.out.println(exchange.getOut().toString());
//                        System.out.println(exchange.getOut().getHeaders().toString());
//                        System.out.println(exchange.getContext().toString());
//
//                        exchange.getOut().setHeader(Exchange.HTTP_URI, simple("http://localhost:8080/camel_test/camel/camelClient"));
//                        exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant("username=zhangsan&password=123"));
//                                    }
//                                })
//                        .to("http://localhost:8080/camel_test/camel/camelService");

                from("direct:start").to("http://localhost:8080/camel_test/camel/camelService");

            }
        };

        context.addRoutes(route);
        context.start();
        sendMessageByRoute(context);



    }

    /**
     * 路由的调用
     * */
    public static void sendMessageByRoute(ModelCamelContext context){



        ProducerTemplate pt = context.createProducerTemplate();

        pt.send("direct:start", new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(Exchange.HTTP_QUERY, constant("username=zhangsan&password=123"));
            }
        });
    }
}
