package com.servlet.user;

import com.dao.UserDaoImpl;
import com.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

        PrintWriter out=response.getWriter();
        try {
            System.out.println("GetAllUsers:正在返回JSON数据");
            List allUsersList = new UserDaoImpl().getAllUsers();

            String jsonSend = JSONUtil.objectToJson(allUsersList);
            out.print(jsonSend);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
