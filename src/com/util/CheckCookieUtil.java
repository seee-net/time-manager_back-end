package com.util;

import com.dao.UserDaoImpl;
import com.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

//TODO 利用Cookie验证身份

public class CheckCookieUtil {
    /**
     * 检查cookie对不对
     *
     * @param request 请求
     * @return 返回boolean值
     */
    public static boolean isCookieRight(HttpServletRequest request) throws IOException {
        //获取Cookie
        String usernameCookie = CookieUtil.getCookieValue(request, "username");
        String passwordCookie = CookieUtil.getCookieValue(request, "password");

        if (!usernameCookie.equals("") && !passwordCookie.equals("")) {
            //Cookie存在
            String username = URLDecoder.decode(usernameCookie, "UTF-8");
            User userInDB = new UserDaoImpl().getUser(username);
            //比对用户密码
            return passwordCookie.equals(userInDB.getPassword());
        }
        return false;
    }
}
