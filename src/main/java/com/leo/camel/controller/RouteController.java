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
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMessage;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.camel.builder.Builder.constant;

import java.io.InputStream;




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
                                .process(new ProcessBegin())
                                .to("http://localhost:8080/camel_test/camel/camelService")
                                .process(new ProcessEnd());
                    }
                };
                camelContext.addRoutes(route);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 开始的processor处理
     */
    private class ProcessBegin implements Processor {
        @Override
        public void process(Exchange exchange) {
            String params = null;
            try {
                HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
                if ("GET".equals(request.getMethod())) {
                    params = request.getQueryString();
                    exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant(params));
                } else {
                    params = exchange.getIn().getBody(String.class);
                    exchange.getOut().setHeader(Exchange.HTTP_METHOD, constant("POST"));
                    exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant(params));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 结束的processor处理
     */
    private class ProcessEnd implements Processor {
        @Override
        public void process(Exchange exchange) {

            InputStream inputStream = null;
            try {
                inputStream = (InputStream) exchange.getIn().getBody();
                byte[] bytes = IOUtils.toByteArray(inputStream);
                exchange.getOut().setBody(bytes);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}


