package com.leo.camel.servlet;

import org.apache.commons.io.IOUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CamelServiceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("这是post请求。。。");
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("这是get请求");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("服务端用户名:" + username + ", 服务端密码:" + password);
//        response.getWriter().write("服务端用户名:" + username + ",服务端密码:" + password);
//        response.getWriter().flush();
//        response.getWriter().close();


        FileInputStream inputStream = new FileInputStream("D:\\temp\\jpg\\cat.jpg");
        byte[] bytes = IOUtils.toByteArray(inputStream);
        response.getOutputStream().write(bytes);
        response.getOutputStream().close();
    }
}
