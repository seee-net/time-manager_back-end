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
import java.util.List;
import java.util.Map;

@WebServlet(name = "ByRoom", urlPatterns = "/ByRoom")
public class ByRoom extends HttpServlet {
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

        List aimdata;

        if (CheckCookieUtil.isCookieRight(request)) {
            //前台传来json
            String jsonReceive = StreamUtil.getInput(in);
            //json转化格式,Date在java.util中，写入数据库可以储存到秒
            Map<String, String> dataReceive = JSONUtil.jsonToMaps(jsonReceive);
            String aimRoom_id = "";

            for (String key : dataReceive.keySet()) {
                switch (key) {
                    case "aimRoom_id": {
                        aimRoom_id = dataReceive.get("aimRoom_id");
                        break;
                    }
                    default:
                        break;
                }
            }
            aimdata = new DataDaoImpl().byRoom(aimRoom_id);

            String jsonSend = JSONUtil.objectToJson(aimdata);

            StreamUtil.setOutput(out, jsonSend);
        } else {
            System.out.println("By_room:Cookie验证失败，重新登陆");
            StreamUtil.setOutput(out, "");
        }
    }
}