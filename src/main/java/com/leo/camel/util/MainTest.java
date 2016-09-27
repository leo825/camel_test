/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/9/27
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
package com.leo.camel.util;

import com.leo.camel.util.ElementNameConverter;
import com.leo.camel.util.JsonSaxAdapter;
import com.leo.camel.util.JsonXmlReader;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

/**
 * Created by Administrator on 2016/9/27.
 */
public class MainTest {

    public static final String JSON = "{\"document\":{\"a\":1,\"b\":2,\"c\":{\"d\":\"text\"},\"e\":[1,2,3],\"f\":[[1,2,3],[4,5,6]], \"g\":null, " +
            "\"h\":[{\"i\":true,\"j\":false}],\"k\":[[{\"l\":1,\"m\":2}],[{\"n\":3,\"o\":4},{\"p\":5,\"q\":6}]]}}";

    public static void main(String[] agrs) throws Exception {

        MainTest test = new MainTest();

        String xml = test.convertToXml(JSON);


        System.out.println(xml);
    }

    public String convertToXml(final String json) throws Exception {
        return convertToXml(json, new JsonXmlReader());
    }


    public String convertToXml(final String json, final JsonXmlReader reader) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        InputSource source = new InputSource(new StringReader(json));
        Result result = new StreamResult(out);
        transformer.transform(new SAXSource(reader, source), result);
        return new String(out.toByteArray());
    }

}
