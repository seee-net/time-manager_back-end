package com.servlet.data;

import com.dao.DataDaoImpl;
import com.util.CheckCookieUtil;
import com.util.JSONUtil;
import com.util.InputUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ByTime", urlPatterns = "/ByTime")
    public class ByTime extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        //定义输入输出流
        BufferedReader in = request.getReader();
        PrintWriter out = response.getWriter();

        List aimdata;

        if (CheckCookieUtil.isCookieRight(request)) {
            //前台传来json
            String jsonReceive = InputUtil.getInput(in);

            Map<String, String> dataReceive = JSONUtil.jsonToMaps(jsonReceive);
            String aimTimeStart = null;
            String aimTimeEnd = null;

            for (String key : dataReceive.keySet()) {
                switch (key) {
                    case "aimTimeStart": {
                        aimTimeStart = dataReceive.get("aimTimeStart");
                        break;
                    }
                    case "aimTimeEnd": {
                        aimTimeStart = dataReceive.get("aimTimeEnd");
                        break;
                    }
                    default:
                        break;
                }
            }
            aimdata = new DataDaoImpl().byTime(aimTimeStart, aimTimeEnd);

            String jsonSend = JSONUtil.objectToJson(aimdata);

            out.print(jsonSend);
        } else {
            System.out.println("ByTime:Cookie验证失败，重新登陆");
            out.print("[]");
        }
    }
}



