package com.entity;

// 用户实体类
public class User {
    private String username;
    private String password;
    private String belong;

    public User(String un, String pw) {
        username = un;
        password = pw;
        belong = "";
    }

    public User(String un, String pw, String bl) {
        username = un;
        password = pw;
        belong = bl;
    }
    public String toString(){ return "用户名：" + username + "密码：" + password; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String un) {
        username = un;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pw) {
        password = pw;
    }

    public String getBelong() { return belong; }

    public void setBelong(String bl) {
        belong = bl;
    }
}
