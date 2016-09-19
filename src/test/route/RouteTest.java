package route;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static org.apache.camel.builder.Builder.constant;

/**
 * Created by Administrator on 2016/9/19.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class RouteTest {


    @Resource
    private CamelContext camelContext;

    @Test
    public void getRouteTest() {

        Route route = camelContext.getRoute("helloRoute5");

        System.out.println(route.getEndpoint() + ":" + route.getId());

        ProducerTemplate pt = camelContext.createProducerTemplate();
        pt.send(route.getEndpoint(), new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(Exchange.HTTP_QUERY, constant("username=zhangsan&password=123"));
            }
        });
    }

    @Test
    public void routeDefinitionTest() throws Exception {

        for (int i = 0; i < 10; i++) {

            final int finalI = i;
            RouteBuilder route = new RouteBuilder() {
                public void configure() throws Exception {
                    from("direct:camelService" + finalI).to("http://localhost:8080/camel_test/camel/camelService");
                }
            };

            camelContext.addRoutes(route);
        }
        sendMessageByRoute(camelContext);
    }


    /**
     * 路由的调用
     */
    public static void sendMessageByRoute(CamelContext context) {


        ProducerTemplate pt = context.createProducerTemplate();

        pt.send("direct:camelService5", new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(Exchange.HTTP_QUERY, constant("username=zhangsan&password=123"));
            }
        });
    }


}
