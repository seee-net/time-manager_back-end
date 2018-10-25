package com.servlet.data;

import com.dao.DataDaoImpl;
import com.entity.Data;
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
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static com.util.CookieUtil.getCookieValue;

@WebServlet(name = "DelData", urlPatterns = "/DelData")
public class DelData extends HttpServlet {
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

        if (CheckCookieUtil.isCookieRight(request)) {
            //得到前台json对象，json转化格式
            String jsonReceive = InputUtil.getInput(in);
            Map<String, Object> dataReceive = JSONUtil.jsonToMaps(jsonReceive);
            //定义json输出对象
            HashMap<String, Object> dataSend = new HashMap<>();

            Data newdata = new Data();

            for (String key : dataReceive.keySet()) {
                switch (key) {
                    case "roomid": {
                        newdata.setRoom_id(dataReceive.get("roomid").toString());
                        break;
                    }
                    case "timestart": {
                        newdata.setTime_start((String) dataReceive.get("timestart"));
                        break;
                    }
                    case "timeend": {
                        newdata.setTime_end((String) dataReceive.get("timeend"));
                        break;
                    }
                    default:
                        break;
                }
            }
            //从Cookie中获取用户名
            String cookieUser = getCookieValue(request, "username");
            newdata.setUsername(URLDecoder.decode(cookieUser, "UTF-8"));

            boolean delDataResult = new DataDaoImpl().DelData(newdata);

            System.out.println("DelData:正在返回JSON数据-删除结果");
            dataSend.put("delResult", delDataResult);

            String jsonSend = JSONUtil.objectToJson(dataSend);

            out.print(jsonSend);
        } else {
            System.out.println("By_room:Cookie验证失败，重新登陆");
            out.print("[]");
        }
    }
}