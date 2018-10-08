package com.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import com.entity.User;
import com.dao.impl.UserDao;
import com.util.DBconn;
import com.util.MD5sett;

// UserDaoImpl 接口实现类
public class UserDaoImpl implements UserDao {
    @Override
    public boolean login(User user){
        String username = user.getUsername();
        String password = user.getPassword();

        String hidPassword = MD5sett.setMD5(username, password);
        user.setPassword(hidPassword);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        boolean userAccess = false;

        try {
            conn = DBconn.getConnection();

            String sql = "select password from all_user " +
                    "where username = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());

            rs = stmt.executeQuery();

            if (rs.next()){
                if (rs.getString("password").equals(user.getPassword()))
                    userAccess = true;
            }
        } catch (SQLException e){
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
        return userAccess;
    }

    @Override
    public boolean register(User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        String hidPassword = MD5sett.setMD5(username, password);

        user.setPassword(hidPassword);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int rowNom = 0;

        try {
            conn = DBconn.getConnection();

            String sql = "insert into all_user (username, password) " +
                         "values(?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            rowNom = stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
        return rowNom != 0;
    }

    @Override
    public boolean chPass(User user, String newPass) {
        if(login(user)) {
            String hidPassword = MD5sett.setMD5(user.getUsername(), newPass);

            user.setPassword(hidPassword);

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            int rowNom = 0;

            try {
                conn = DBconn.getConnection();

                String sql = "UPDATE all_user " +
                             "SET password = ? WHERE username = ?";

                stmt = conn.prepareStatement(sql);
                stmt.setString(1, hidPassword);
                stmt.setString(2, user.getUsername());

                rowNom = stmt.executeUpdate();
            } catch (SQLException e){
                System.err.println("DAO: 数据库错误");
                e.printStackTrace();
            } finally {
                DBconn.closeAll(conn, stmt, rs);
            }

            return rowNom != 0;
        }
        return false;
    }

    @Override
    public User getUser(String username){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        User user = new User(username,"");

        try {
            conn = DBconn.getConnection();

            String sql = "select password from all_user " +
                         "where username = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            if(rs.next())
                 user.setPassword(rs.getString("password"));
        } catch (SQLException e){
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
        return user;
    }

    @Override
    public User getUserInfo(String username){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        User user = new User(username,"", "");

        try {
            conn = DBconn.getConnection();

            String sql = "select user_belong from all_user " +
                    "where username = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            if(rs.next())
                user.setBelong(rs.getString("user_belong"));
        } catch (SQLException e){
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<User> allUsers = new ArrayList<>();

        try {
            conn = DBconn.getConnection();

            String sql = "select * from all_user";

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();

            while(rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");

                User user = new User(username, password);
                allUsers.add(user);
            }
        } catch (SQLException e){
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
        return  allUsers;
    }
}
