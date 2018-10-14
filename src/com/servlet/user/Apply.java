package com.servlet.user;

import com.dao.DataDaoImpl;
import com.util.CookieUtil;
import com.util.JSONUtil;
import com.util.StreamUtil;
import  com.entity.data;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import static com.util.CookieUtil.getCookieValue;
import static com.util.FormDate.setFormDate;
@WebServlet(name = "Apply", urlPatterns = "/Apply")
public class Apply extends HttpServlet{
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

        if (CookieUtil.isCookieRight(request, out)) {
            //前台传来json
            String jsonReceive = StreamUtil.getInput(in);
            //json转化格式,Date在java.util中，写入数据库可以储存到秒
            Map<String, Object> dataReceive = JSONUtil.jsonToMaps(jsonReceive);

            data newdata = new data();

            for (String key : dataReceive.keySet()) {
                switch (key) {
                    case "room_id": {
                        newdata.setRoom_id((Integer) dataReceive.get("room_id")) ;
                        break;
                    }
                    case "time_start": {
                        newdata.setTime_start((String) dataReceive.get("time_start")) ;
                        break;
                    }
                    case "time_end": {
                        newdata.setTime_end((String) dataReceive.get("time_end")) ;
                        break;
                    }
                    default:
                        break;
                }
            }
            String  cookieuser = getCookieValue(request,"username");

            newdata.setUsername(URLDecoder.decode(cookieuser, "UTF-8"));

            boolean applyResult = new DataDaoImpl().Apply(newdata);
            System.out.println("Register:正在返回JSON数据");
            HashMap<String, Object> dataSend = new HashMap<>();

            dataSend.put("username", newdata.getUsername());
            dataSend.put("room_id", newdata.getRoom_id());
            dataSend.put("time_start", newdata.getTime_start());
            dataSend.put("time_end", newdata.getTime_end());

            dataSend.put("applyResult", applyResult);

            String jsonSend = JSONUtil.objectToJson(dataSend);

            StreamUtil.setOutput(out, jsonSend);
        } else {
            System.out.println("Cookie验证失败，重新登陆");
            new DataDaoImpl().delOldDate();
            StreamUtil.setOutput(out, "");
        }
    }
}

