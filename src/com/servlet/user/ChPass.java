package com.servlet.user;

import com.dao.UserDaoImpl;
import com.entity.User;

import com.util.CookieUtil;
import com.util.StreamUtil;
import com.util.JSONUtil;

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

@WebServlet(name = "ChPass", urlPatterns = "/ChPass")
public class ChPass extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        ServletInputStream in =request.getInputStream();
        ServletOutputStream out=response.getOutputStream();

        try {
            //获取Cookie
            String usernameCookie = CookieUtil.getCookieValue(request, "username");
            String passwordCookie = CookieUtil.getCookieValue(request, "password");

            //判断Cookie是否存在
            if(!usernameCookie.equals("") && !passwordCookie.equals("")) {
                System.out.println("ChPass:检测存在Cookie");
                //Cookie存在
                //比对数据库中内容
                String username = URLDecoder.decode(usernameCookie, "UTF-8");
                System.out.println("ChPass:解码后的用户名" + username);
                User userInDB = new UserDaoImpl().getUser(username);
                System.out.println("ChPass:正在比对Cookie中的密码");
                //比对用户密码
                if(passwordCookie.equals(userInDB.getPassword())){
                    //密码正确
                    //返回JSON数据
                    String jsonReceive = StreamUtil.getInput(in);

                    Map<String, String> dataReceive = JSONUtil.jsonToMaps(jsonReceive);
                    String oldPassword = "";
                    String newPassword = "";

                    //TODO 替换（工具类）

                    for(String key : dataReceive.keySet()){
                        switch (key){
                            case "oldPassword": {
                                oldPassword = dataReceive.get("oldPassword");
                                break;
                            }
                            case "newPassword": {
                                newPassword = dataReceive.get("newPassword");
                                break;
                            }
                            default:
                                break;
                        }
                    }
                    User user = new User(username, oldPassword);
                    System.out.println(user.toString());

                    boolean userCh = new UserDaoImpl().chPass(user, newPassword);

                    System.out.println("ChPass:Cookie中密码正确");
                    System.out.println("ChPass:正在返回JSON数据");
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("userCh", userCh);

                    String jsonSend = JSONUtil.objectToJson(data);
                    StreamUtil.setOutput(out, jsonSend);
                }else {
                    //密码错误
                    //重设Cookie
                    System.out.println("ChPass:密码错误");
                    System.out.println("ChPass:正在清除错误的Cookie");
                    CookieUtil.removeCookie(response, "username");
                    CookieUtil.removeCookie(response, "password");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
