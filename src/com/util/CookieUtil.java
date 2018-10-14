package com.util;

import com.dao.UserDaoImpl;
import com.entity.User;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Cookie工具类
 *
 */
public class CookieUtil {

    public CookieUtil() {
    }

    /**
     * 添加cookie
     *
     * @param response 响应
     * @param name     Cookie名称
     * @param value    Cookie值
     * @param maxAge   保留期限
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response 响应
     * @param name     Cookie名称
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie uid = new Cookie(name, null);
        uid.setPath("/");
        uid.setMaxAge(0);
        response.addCookie(uid);
    }

    /**
     * 获取cookie值
     *
     * @param request 请求
     * @return 返回Cookie值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }

    /**
     * 检查cookie对不对
     *
     * @param request 请求
     * @return 返回boolean值
     */
    public static boolean isCookieRight(HttpServletRequest request,ServletOutputStream out) throws IOException {
        //获取Cookie
        String usernameCookie = CookieUtil.getCookieValue(request, "username");
        String passwordCookie = CookieUtil.getCookieValue(request, "password");

        if (!usernameCookie.equals("") && !passwordCookie.equals("")) {
            //Cookie存在
            String username = URLDecoder.decode(usernameCookie, "UTF-8");
            User userInDB = new UserDaoImpl().getUser(username);
            //比对用户密码
            if (passwordCookie.equals(userInDB.getPassword())) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("username", userInDB.getUsername());
                data.put("password", userInDB.getPassword());
                data.put("userAccess", "true");

                String jsonSend = JSONUtil.objectToJson(data);

                StreamUtil.setOutput(out, jsonSend);
                return true;
            }
            return false;
        }
        return false;
    }

    public static void setLogCookie(HttpServletRequest request,
                                   HttpServletResponse response,
                                   ServletInputStream in,
                                   ServletOutputStream out) throws IOException {
        if(! isCookieRight(request,out) ){
            //第一步：不管有无Cookie,都将其置零
            CookieUtil.removeCookie(response, "username");
            CookieUtil.removeCookie(response, "password");
            //第二步：设置Cookie

            //定义输入输出流
            in =request.getInputStream();
            out=response.getOutputStream();
            try {
                String jsonReceive = StreamUtil.getInput(in);

                Map<String, String> dataReceive = JSONUtil.jsonToMaps(jsonReceive);
                String username = "";
                String password = "";
                //前台检查jsonReceive是否为空
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

                User user = new User(username, password);

                boolean userAccess = new UserDaoImpl().login(user);

                if (userAccess) {

                    user.setUsername(URLEncoder.encode(user.getUsername(), "UTF-8"));
                    CookieUtil.addCookie(response, "username", user.getUsername(), 3 * 3600 * 24);
                    CookieUtil.addCookie(response, "password", user.getPassword(), 3 * 3600 * 24);
                }

                HashMap<String, Object> data = new HashMap<>();
                data.put("username", user.getUsername());
                data.put("password", user.getPassword());
                data.put("userAccess", userAccess);

                String jsonSend = JSONUtil.objectToJson(data);

                StreamUtil.setOutput(out, jsonSend);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
