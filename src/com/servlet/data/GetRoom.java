package com.servlet.data;

import com.dao.DataDaoImpl;
import com.util.CheckCookieUtil;
import com.util.JSONUtil;
import com.util.StreamUtil;
import com.entity.Room;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        ServletOutputStream out = response.getOutputStream();

        List<Room> roomData;

        if (CheckCookieUtil.isCookieRight(request)) {

            roomData = new DataDaoImpl().getRoom();
            String jsonSend = JSONUtil.objectToJson(roomData);
            StreamUtil.setOutput(out, jsonSend);

        } else {
            System.out.println("Room:Cookie验证失败，重新登陆");
            StreamUtil.setOutput(out, "");
        }
    }
}

