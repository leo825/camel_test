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
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMessage;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.camel.builder.Builder.constant;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;


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


    @RequestMapping(value = "/getRouteSize", method = {RequestMethod.GET, RequestMethod.POST})
    public void getRouteSize(HttpServletRequest request, HttpServletResponse response) throws Exception {


        String result1 = "route开始的长度：" + camelContext.getRoutes().size();

        List<Route> routeList = camelContext.getRoutes();

        for (Route route : routeList) {
            if ("Endpoint[servlet:///0]".equals(route.getEndpoint().toString())) {
                System.out.println("找到了");
                System.out.println("------------" + route.getId() + "," + route.getEndpoint().toString());
                camelContext.stopRoute(route.getId());
                camelContext.removeRoute(route.getId());
            }
        }


        String result2 = "route删除节点后的长度：" + camelContext.getRoutes().size();


        System.out.println(result1 + "," + result2);

        PrintWriter out = response.getWriter();
        out.write(result1 + "," + result2);
        out.flush();
        out.close();
    }

}


