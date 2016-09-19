package com.leo.camel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.annotation.Resource;


/**
 * Created by Administrator on 2016/9/12.
 */
@WebServlet(name = "camelClient", urlPatterns = "/camel/camelClient")
public class CamleClientServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("用户名:" + username + ", 密码:" + password);
        response.getWriter().write("用户名:" + username + ", 密码:" + password);
        response.getWriter().flush();
        response.getWriter().close();

    }
}
