package com.dao;

import com.dao.impl.DataDao;
import com.entity.Room;
import com.entity.Data;
import com.util.DBconn;
import com.util.FormDate;

import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

public class DataDaoImpl implements DataDao {
    @Override
    public void delOldDate() {
        //数据库设置
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBconn.getConnection();

            Calendar now = Calendar.getInstance();
            String year = FormDate.addZero(now.get(Calendar.YEAR));
            String month = FormDate.addZero(now.get(Calendar.MONTH) + 1) ;
            String day = FormDate.addZero(now.get(Calendar.DAY_OF_MONTH));
            String hour = FormDate.addZero(now.get(Calendar.HOUR_OF_DAY));
            String minute = FormDate.addZero(now.get(Calendar.MINUTE));
            String second = FormDate.addZero(now.get(Calendar.SECOND));

            String sql = "DELETE FROM user_room " +
                    "Where time_end < ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, year+month+day+hour+minute+second);
            stmt.executeUpdate() ;

        } catch (SQLException e) {
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
    }

    public boolean DelData(Data deldata){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int rowNom = 0;


        try {
            conn = DBconn.getConnection();

            String sql = "delete from user_room " +
                    "where username = ? and room_id = ? and time_start = ? and time_end = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, deldata.getUsername());
            stmt.setString(2, deldata.getRoom_id());
            stmt.setTimestamp(3, Timestamp.valueOf(deldata.getTime_start()));
            stmt.setTimestamp(4, Timestamp.valueOf(deldata.getTime_end()));

            rowNom = stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
        return rowNom != 0;
    }

    public boolean Apply(Data newdata){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int rowNom = 0;

        try {
            conn = DBconn.getConnection();

            String sql = "insert into user_room (username, room_id,time_start,time_end) " +
                    "values(?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newdata.getUsername());
            stmt.setString(2, newdata.getRoom_id());
            stmt.setTimestamp(3, Timestamp.valueOf(newdata.getTime_start()));
            stmt.setTimestamp(4, Timestamp.valueOf(newdata.getTime_end()));

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
    public List<Data> byRoom(String aimRoom_id) {
        //数据库设置
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Data> aimdata = new ArrayList<>();

        delOldDate();
        try {
            conn = DBconn.getConnection();

            String sql = "select * from user_room " +
                    "where room_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, aimRoom_id);

            rs = stmt.executeQuery();

            while (rs.next()){
                Data dataInDB = new Data();
                dataInDB.setUsername(rs.getString("username"));
                dataInDB.setRoom_id(aimRoom_id);
                dataInDB.setTime_start( rs.getString("time_start") );
                dataInDB.setTime_end( rs.getString("time_end") );

                aimdata.add(dataInDB);
            }
        } catch (SQLException e){
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
        return aimdata;
    }

    @Override
    public List<Data> byTime(String aimTimeStart,String aimTimeEnd) {

        //数据库设置
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Data> aimdata = new ArrayList<>();

        delOldDate();
        try {
            conn = DBconn.getConnection();

            String sql = "select * from user_room " +
                    "where (? between time_start AND time_end) OR (? between time_start AND time_end)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, aimTimeStart);
            stmt.setString(2, aimTimeEnd);

            rs = stmt.executeQuery();

            while (rs.next()){
                Data dataInDB = new Data();
                dataInDB.setUsername(rs.getString("username"));
                dataInDB.setRoom_id(rs.getString("room_id"));
                dataInDB.setTime_start( rs.getString("time_start") );
                dataInDB.setTime_end( rs.getString("time_end") );

                aimdata.add(dataInDB);
            }
        } catch (SQLException e){
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
        return aimdata;
    }

    public List<Room> getRoom(){
        //数据库设置
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Room> roomData = new ArrayList<>();
        try {
            conn = DBconn.getConnection();

            String sql = "select * from all_room " ;

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();

            while (rs.next()){
                Room room = new Room();
                room.setRoomid(rs.getInt("room_id"));
                room.setRoomname(rs.getString("room_name"));

                roomData.add(room);
            }
        } catch (SQLException e){
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
        return roomData;
    }


    public List<Data> getUserData(String username) {

        //数据库设置
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Data> aimdata = new ArrayList<>();

        delOldDate();
        try {
            conn = DBconn.getConnection();

            String sql = "SELECT * FROM all_room NATURAL JOIN user_room " +
                    "WHERE username = ? ";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            while (rs.next()){
                Data dataInDB = new Data();
                dataInDB.setUsername(rs.getString("username"));
                dataInDB.setRoom_name(rs.getString("room_name"));
                dataInDB.setTime_start( rs.getString("time_start") );
                dataInDB.setTime_end( rs.getString("time_end") );

                aimdata.add(dataInDB);
            }
        } catch (SQLException e){
            System.err.println("DAO: 数据库错误");
            e.printStackTrace();
        } finally {
            DBconn.closeAll(conn, stmt, rs);
        }
        return aimdata;
    }
}
