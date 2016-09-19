/***
 * *author Administrator
 * *date 2016/9/13
 ***/
package com.leo.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.camel.builder.Builder.constant;


/**
 * Created by Administrator on 2016/9/13.
 */
@Component
public class ProcessorTest implements Processor {

    public void process(Exchange exchange) throws Exception {
        HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
        System.out.println(request.getQueryString());
        exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant(request.getQueryString()));
    }


}