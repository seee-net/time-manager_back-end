package com.servlet.user;

import com.dao.UserDaoImpl;
import com.entity.User;
import com.util.InputUtil;
import com.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Register", urlPatterns = "/Register")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        BufferedReader in =request.getReader();
        PrintWriter out=response.getWriter();
        try {
            System.out.println("Register:正在获取表单数据");
            String jsonReceive = InputUtil.getInput(in);

            Map<String, String> dataReceive = JSONUtil.jsonToMaps(jsonReceive);
            String username = "";
            String password = "";

            if(dataReceive != null) {
                for (String key : dataReceive.keySet()) {
                    switch (key) {
                        case "username": {
                            username = dataReceive.get("username");
                            break;
                        }
                        case "password": {
                            password = dataReceive.get("password");
                            break;
                        }
                        default:
                            break;
                    }
                }
            }

            User user = new User(username, password);
            boolean userRegister = new UserDaoImpl().register(user);
            System.out.println("Register:正在返回JSON数据");
            HashMap<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("password", password);
            data.put("userRegister", userRegister);

            String jsonSend = JSONUtil.objectToJson(data);
            out.print(jsonSend);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
