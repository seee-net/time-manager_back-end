package com.servlet.data;

import com.dao.DataDaoImpl;
import com.entity.Data;
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
        ServletInputStream in = request.getInputStream();
        ServletOutputStream out = response.getOutputStream();

        if (CheckCookieUtil.isCookieRight(request)) {
            //得到前台json对象，json转化格式
            String jsonReceive = StreamUtil.getInput(in);
            Map<String, Object> dataReceive = JSONUtil.jsonToMaps(jsonReceive);
            //定义json输出对象
            HashMap<String, Object> dataSend = new HashMap<>();

            Data newdata = new Data();

            for (String key : dataReceive.keySet()) {
                switch (key) {
                    case "room_id": {
                        newdata.setRoom_id(dataReceive.get("room_id").toString());
                        break;
                    }
                    case "time_start": {
                        newdata.setTime_start((String) dataReceive.get("time_start"));
                        break;
                    }
                    case "time_end": {
                        newdata.setTime_end((String) dataReceive.get("time_end"));
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
            dataSend.put("applyResult", delDataResult);

            String jsonSend = JSONUtil.objectToJson(dataSend);

            StreamUtil.setOutput(out, jsonSend);
        } else {
            System.out.println("By_room:Cookie验证失败，重新登陆");
            StreamUtil.setOutput(out, "");
        }
    }
}