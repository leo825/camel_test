/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/10/8
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
package com.leo.camel.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/10/8.
 */
public class DataTestServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String data = getRequestByPost("http://192.168.0.90:2087/90/wfs", "VERSION=1.1.0&SERVICE=WFS&BODY=<?xml version=\"1.0\"?><GetFeature><Query typeName=\"CS_BGFD_PT\"><Filter><PropertyIsLike><PropertyName>MC</PropertyName><Literal>天津%25</Literal></PropertyIsLike></Filter></Query></GetFeature>");
        System.out.println(data);
        System.out.println("hello world=========================121");
        response.setHeader("Content-type", "text/html;charset=GBK");
        response.getWriter().write(data);
        response.getWriter().flush();
        response.getWriter().close();


    }


    public static String getRequestByPost(String url, String params) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoOutput(true);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(params);
        out.flush();
        out.close();

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }
}
