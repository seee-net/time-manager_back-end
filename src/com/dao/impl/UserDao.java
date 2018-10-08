package com.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.entity.User;

// 用户Dao接口
public interface UserDao {
    public boolean login(User user);
    public boolean register(User user);
    public boolean chPass(User user, String newPass);
    public User getUser(String username);
    public User getUserInfo(String username);
    public List<User> getAllUsers();
}
