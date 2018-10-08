package com.servlet.user;

import com.dao.UserDaoImpl;
import com.entity.User;
import com.util.CookieUtil;
import com.util.JSONUtil;
import com.util.StreamUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;

@WebServlet(name = "GetUserInfo", urlPatterns = "/GetUserInfo")
public class GetUserInfo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        ServletOutputStream out=response.getOutputStream();
        try {
            //获取Cookie
            String usernameCookie = CookieUtil.getCookie(request, "username");
            String passwordCookie = CookieUtil.getCookie(request, "password");

            //判断Cookie是否存在
            if(!usernameCookie.equals("") && !passwordCookie.equals("")) {
                System.out.println("GetUserInfo:检测存在Cookie");
                //Cookie存在
                //比对数据库中内容
                String username = URLDecoder.decode(usernameCookie, "UTF-8");
                System.out.println("GetUserInfo:解码后的用户名" + username);
                User userInDB = new UserDaoImpl().getUser(username);
                System.out.println("GetUserInfo:正在比对Cookie中的密码");
                //比对用户密码
                //TODO 验证Cookie
                if(passwordCookie.equals(userInDB.getPassword())){
                    //密码正确
                    //返回JSON数据
                    System.out.println("GetUserInfo:密码正确");
                    System.out.println("GetUserInfo:正在返回JSON数据");
                    userInDB = new UserDaoImpl().getUserInfo(username);
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("username", userInDB.getUsername());
                    data.put("belong", userInDB.getBelong());

                    String jsonSend = JSONUtil.objectToJson(data);

                    StreamUtil.setOutput(out, jsonSend);
                }else {
                    //密码错误
                    //重设Cookie
                    System.out.println("GetUserInfo:密码错误");
                    System.out.println("GetUserInfo:正在清除错误的Cookie");
                    CookieUtil.removeCookie(response, "username");
                    CookieUtil.removeCookie(response, "password");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
