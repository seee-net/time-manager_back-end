package com.servlet.data;

import com.dao.DataDaoImpl;
import com.util.CheckCookieUtil;
import com.util.JSONUtil;
import com.entity.Room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetRoom", urlPatterns = "/GetRoom")
public class GetRoom extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        //定义输出流
        PrintWriter out = response.getWriter();

        List<Room> roomData;

        if (CheckCookieUtil.isCookieRight(request)) {

            roomData = new DataDaoImpl().getRoom();
            String jsonSend = JSONUtil.objectToJson(roomData);
            out.print(jsonSend);

        } else {
            System.out.println("Room:Cookie验证失败，重新登陆");
            out.print("[]");
        }
    }
}

