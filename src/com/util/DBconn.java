package com.util;

import java.sql.*;

public class DBconn {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/timetables";
    private static final String USER = "timetables";
    private static final String PASS = "5BQCjGPRVw2hKmGm";

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("DBconn: 数据库加载驱动失败");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        try{
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }catch (SQLException e){
            System.err.println("DBconn: 数据库关闭连接失败");
            e.printStackTrace();
        }
    }
}
