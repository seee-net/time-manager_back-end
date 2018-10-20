package com.servlet.data;

import com.dao.DataDaoImpl;
import com.util.CheckCookieUtil;
import com.util.FormDate;
import com.util.JSONUtil;
import com.util.StreamUtil;
import com.entity.Data;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.util.CookieUtil.getCookieValue;

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


        if (CheckCookieUtil.isCookieRight(request)) {
            //得到前台json对象，json转化格式
            String jsonReceive = StreamUtil.getInput(in);
            Map<String, Object> dataReceive = JSONUtil.jsonToMaps(jsonReceive);
            //定义json输出对象
            HashMap<String, Object> dataSend = new HashMap<>();

            String startTime = (String)dataReceive.get("time_start");
            String endTime = (String)dataReceive.get("time_end");

            //比较时间关系
            if(startTime.compareTo(endTime) >= 0){
                System.out.println("Apply:正在返回JSON数据-与结束时间比较结果");
                dataSend.put("applyResult", "例会结束时间错误");
            }
            else {
                Data newdata = new Data();

                for (String key : dataReceive.keySet()) {
                    switch (key) {
                        case "room_id": {
                            newdata.setRoom_id(dataReceive.get("room_id").toString());
                            break;
                        }
                        case "time_start": {
                            newdata.setTime_start(startTime);
                            break;
                        }
                        case "time_end": {
                            newdata.setTime_end(endTime);
                            break;
                        }
                        default:
                            break;
                    }
                }

                //用byRoom方法获取一个目标房子的user_room关系
                List<Data> aimData = new DataDaoImpl().byRoom(newdata.getRoom_id());

                //遍历，检查是否可以Apply
                boolean canApply = true;
                for (Data item : aimData) {
                    //获取item的开始，结束时间
                    String item_st = item.getTime_start();
                    String item_et = item.getTime_end();

                    if (startTime.compareTo(item_st) >= 0 && startTime.compareTo(item_et) < 0) {
                        System.out.println("Apply:正在返回JSON数据-目标开始时间比较结果");
                        dataSend.put("applyResult", "例会开始时间冲突");
                        canApply = false;
                        break;
                    } else if (endTime.compareTo(item_st) > 0 && endTime.compareTo(item_et) <= 0) {
                        System.out.println("Apply:正在返回JSON数据-目标结束时间比较结果");
                        dataSend.put("applyResult", "例会结束时间冲突");
                        canApply = false;
                        break;
                    }
                }

                if (canApply) {
                    //从Cookie中获取用户名
                    String cookieUser = getCookieValue(request, "username");
                    newdata.setUsername(URLDecoder.decode(cookieUser, "UTF-8"));

                    boolean applyResult = new DataDaoImpl().Apply(newdata);
                    System.out.println("Apply:正在返回JSON数据-插入结果");
                    dataSend.put("applyResult", applyResult);
                }

            }
            //转为json格式，并传给前台
            String jsonSend = JSONUtil.objectToJson(dataSend);
            StreamUtil.setOutput(out, jsonSend);
        } else {
            System.out.println("Apply:Cookie验证失败，重新登陆");
            StreamUtil.setOutput(out, "");
        }
    }
}

