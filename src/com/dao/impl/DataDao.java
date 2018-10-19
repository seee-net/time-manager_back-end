package com.dao.impl;

//接口
//TODO 接口

import java.util.HashMap;
import java.util.List;

import com.entity.Room;
import com.entity.data;

public interface DataDao {
    public void delOldDate();
    public boolean Apply(data newdata);
    public List<data> byRoom(String aimRoom_id);
    public List<data> byTime(String aimTimeStart, String aimTimeEnd);
    public List<Room> getRoom();
    public List<data> getUserData(String username);
}
