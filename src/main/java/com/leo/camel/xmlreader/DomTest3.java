/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/9/29
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
package com.leo.camel.xmlreader;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 使用递归解析给定的任意一个XML文档并且将其内容输出到命名行上
 *
 * @author xukunhui
 */
public class DomTest3 {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(new File("D:\\temp\\xml\\test.xml"));

        //获得根元素结点
        Element root = doc.getDocumentElement();


        NodeList featureMemberList = doc.getElementsByTagName("gml:featureMember");


        for (int i = 0; i < featureMemberList.getLength(); i++) {
            Node node = featureMemberList.item(i);

            String tagName = node.getNodeName();


        }









        /*
         * 拿到根元素之后，从这边开始进行递归了
         * 递归方法的接受的参数就是每次给定的元素，现在传一个根元素，根解析完之后碰到一个新的子元素，则重复这个递归解析的方法，递归方法每次传递都是一个Element对象
         * 递归的出口就是没有包含子元素的时候，就是普通文本的时候输出文本就可以了
         */
//        parseElement(root);

    }

    //递归的方法
    private static void parseElement(Element element) {

        //获得元素的名字
        String tagName = element.getNodeName();

        //获得element的子元素列表，这个子元素里面有可能是文本，也有可能是元素或者两者都有，所有要进行判断
        NodeList children = element.getChildNodes();
        System.out.print("<" + tagName);

        //element元素的所有属性所构成的NamedNodeMap对象，需要对齐进行判断
        NamedNodeMap map = element.getAttributes();
        if (null != map) {
            for (int i = 0; i < map.getLength(); i++) {
                //获得改元素的每一个属性
                Attr attr = (Attr) map.item(i);
                String attrName = attr.getName();
                String attrValue = attr.getValue();

                System.out.print(" " + attrName + "=\"" + attrValue + "\"");

            }
        }
        System.out.print(">");

        //解析它的孩子节点
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);

            //获得节点类型
            short nodeType = node.getNodeType();

            //如果这个孩子节点是元素，则进行递归重复这个方法
            if (nodeType == Node.ELEMENT_NODE) {
                parseElement((Element) node);
            }

            //如果是文本，则直接打印,这也是递归的出口
            else if (nodeType == Node.TEXT_NODE) {
                System.out.print(node.getNodeValue());
            }

            //如果是一个注释
            else if (nodeType == Node.COMMENT_NODE) {
                System.out.print("<!--");
                Comment comment = (Comment) node;

                //注释的内容
                String data = comment.getData();
                System.out.print(data);
                System.out.print("-->");
            }
        }

        System.out.print("</" + tagName + ">");
    }
}