package com.servlet.user;

import com.dao.UserDaoImpl;
import com.entity.User;
import com.util.StreamUtil;
import com.util.JSONUtil;
import com.util.CookieUtil;

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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Login", urlPatterns = "/Login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        //定义输入输出流
        ServletInputStream in =request.getInputStream();
        ServletOutputStream out=response.getOutputStream();

        //获取Cookie
        String usernameCookie = CookieUtil.getCookieValue(request, "username");
        String passwordCookie = CookieUtil.getCookieValue(request, "password");

        //判断Cookie是否存在
        if(!usernameCookie.equals("") && !passwordCookie.equals("")) {
            System.out.println("Login:检测存在Cookie");
            //Cookie存在
            //比对数据库中内容
            String username = URLDecoder.decode(usernameCookie, "UTF-8");
            System.out.println("Login:解码后的用户名" + username);
            User userInDB = new UserDaoImpl().getUser(username);
            System.out.println("Login:正在比对Cookie中的密码");
            //比对用户密码
            if(passwordCookie.equals(userInDB.getPassword())){
                //密码正确
                //返回JSON数据
                System.out.println("Login:密码正确");
                System.out.println("Login:正在返回JSON数据");
                HashMap<String, Object> data = new HashMap<>();
                data.put("username", userInDB.getUsername());
                data.put("password", userInDB.getPassword());
                data.put("userAccess", "true");

                String jsonSend = JSONUtil.objectToJson(data);

                StreamUtil.setOutput(out, jsonSend);
            }else {
                //密码错误
                //重设Cookie
                System.out.println("Login:密码错误");
                System.out.println("Login:正在清除错误的Cookie");
                CookieUtil.removeCookie(response, "username");
                CookieUtil.removeCookie(response, "password");
            }
        } else {
            //Cookie不存在
            System.out.println("Login:没有检测到Cookie");
            try {
                String jsonReceive = StreamUtil.getInput(in);

                Map<String, String> dataReceive = JSONUtil.jsonToMaps(jsonReceive);
                String username = "";
                String password = "";

                if(dataReceive != null){
                    for(String key : dataReceive.keySet()){
                        switch (key){
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

                if(!user.getUsername().equals("")) {
                    //表单登录
                    System.out.println("Login:检测存在表单内容");
                    boolean userAccess = new UserDaoImpl().login(user);

                    if (userAccess) {
                        System.out.println("Login:密码正确");
                        System.out.println("Login:正在设置Cookie");

                        user.setUsername(URLEncoder.encode(user.getUsername(), "UTF-8"));
                        CookieUtil.addCookie(response,"username", user.getUsername(), 3 * 3600 * 24);
                        CookieUtil.addCookie(response,"password", user.getPassword(), 3 * 3600 * 24);
                    }else{
                        System.out.println("Login:密码错误");
                    }

                    System.out.println("Login:正在返回JSON数据");
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("username", user.getUsername());
                    data.put("password", user.getPassword());
                    data.put("userAccess", userAccess);

                    String jsonSend = JSONUtil.objectToJson(data);

                    StreamUtil.setOutput(out, jsonSend);
                } else {
                    System.out.println("Login:没有检测到表单内容");
                    StreamUtil.setOutput(out, "");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
