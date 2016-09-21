/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/9/19
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
package com.leo.camel.controller;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.camel.builder.Builder.constant;

/**
 * Created by Administrator on 2016/9/19.
 */

@Controller
@RequestMapping("/http")
public class RouteController {


    @Resource
    private CamelContext camelContext;

    @PostConstruct
    public void init() {

        String testUrl = "http://192.168.0.101/PGIS_S_TileMapServer/Maps/vec_tj/EzMap";
        try {
            for (int i = 0; i < 10; i++) {

                final int finalI = i;
                RouteBuilder route = new RouteBuilder() {
                    public void configure() throws Exception {
                        from("servlet:///" + finalI)
                                .process(new Processor() {
                                             @Override
                                             public void process(Exchange exchange) {
                                                 try {
                                                     HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
                                                     String str = exchange.getIn().getBody(String.class);
                                                     System.out.println(request.getQueryString());
                                                     System.out.println(str);
                                                     exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant(request.getQueryString()));
                                                     //exchange.getOut().setBody(request.getQueryString());
                                                 } catch (Exception e) {
                                                     e.printStackTrace();
                                                 }
                                             }
                                         }

                                ).to("http://192.168.0.101/PGIS_S_TileMapServer/Maps/vec_tj/EzMap");
                    }
                };
                camelContext.addRoutes(route);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/route", method = {RequestMethod.GET, RequestMethod.POST})
    public void getRoute(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("2222222222223");

        ProducerTemplate pt = camelContext.createProducerTemplate();


        pt.send("direct:5", new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(Exchange.HTTP_QUERY, constant(request.getQueryString()));
            }
        });
    }

}


