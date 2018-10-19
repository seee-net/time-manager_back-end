package com.dao.impl;

import java.util.List;

import com.entity.Room;
import com.entity.Data;

public interface DataDao {
    void delOldDate();
    boolean Apply(Data newdata);

    List<Data> byRoom(String aimRoom_id);
    List<Data> byTime(String aimTimeStart, String aimTimeEnd);
    List<Room> getRoom();
    List<Data> getUserData(String username);
}
