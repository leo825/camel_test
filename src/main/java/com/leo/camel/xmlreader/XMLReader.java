/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/9/29
 * Time: 10:11
 * To change this template use File | Settings | File Templates.
 */
package com.leo.camel.xmlreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

/**
 * Created by Administrator on 2016/9/29.
 */
public class XMLReader {

    public static void getXMLInfo() {

        try {

            SAXReader sr = new SAXReader();//获取读取xml的对象。
            Document resultDoc = (Document) sr.read("D:\\temp\\xml\\test.xml");//得到xml所在位置。然后开始读取。并将数据放入doc中
            Element rootEle = resultDoc.getRootElement();
            String nsUri = rootEle.getNamespaceURI();
            Map nsMap = new HashMap();
            nsMap.put("wfs", "http://www.opengis.net/wfs");
            nsMap.put("gml", "http://www.opengis.net/ogc");
            nsMap.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            XPath mesXpath = resultDoc.createXPath("//gml:featureMember");
            mesXpath.setNamespaceURIs(nsMap);
            List<Node> mesList = mesXpath.selectNodes(resultDoc);

            List<Element> childElements = rootEle.elements();

            for (Element child : childElements) {

                List<Element> elementList = child.elements();
                for (Element ele : elementList) {
                    System.out.println(ele.getName() + ": " + ele.attributeValue("id"));
                }
                System.out.println();

                //已知子元素名的情况下
//                System.out.println("title" + child.elementText("title"));
//                System.out.println("author" + child.elementText("author"));
                //这行是为了格式化美观而存在
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] agrs) {
        getXMLInfo();
    }


    public void getElementValue(Element root) {

    }
}
