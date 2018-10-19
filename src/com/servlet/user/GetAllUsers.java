package com.servlet.user;

import com.dao.UserDaoImpl;

import com.util.StreamUtil;
import com.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetAllUsers")
public class GetAllUsers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        ServletInputStream in =request.getInputStream();
        ServletOutputStream out=response.getOutputStream();
        try {
            System.out.println("GetAllUsers:正在返回JSON数据");
            List allUsersList = new UserDaoImpl().getAllUsers();

            String jsonSend = JSONUtil.objectToJson(allUsersList);
            StreamUtil.setOutput(out, jsonSend);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
