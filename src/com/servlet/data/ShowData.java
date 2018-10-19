package com.servlet.data;

import com.dao.DataDaoImpl;

import com.util.CheckCookieUtil;

import com.util.JSONUtil;
import com.util.StreamUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;


import static com.util.CookieUtil.getCookieValue;

@WebServlet(name = "ShowData", urlPatterns = "/ShowData")
public class ShowData extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        //定义输入输出流
        ServletInputStream in = request.getInputStream();
        ServletOutputStream out = response.getOutputStream();
        //从Cookie中获取用户名
        String  cookieuser = getCookieValue(request,"username");
        String loginUsername = URLDecoder.decode(cookieuser, "UTF-8");

        if (CheckCookieUtil.isCookieRight(request)) {
            List aimdata;
            aimdata = new DataDaoImpl().getUserData(loginUsername);

            System.out.println("ShowData:正在返回JSON数据");
            String jsonSend = JSONUtil.objectToJson(aimdata);

            StreamUtil.setOutput(out, jsonSend);
        } else {
            System.out.println("Cookie验证失败，重新登陆");
            new DataDaoImpl().delOldDate();
            StreamUtil.setOutput(out, "");
        }
    }
}
