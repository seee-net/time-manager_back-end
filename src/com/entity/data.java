package com.entity;

import java.util.Date;

import static com.util.FormDate.setFormDate;


public class data {
    private String username;
    private int room_id;
    private String time_start;
    private String time_end;

    public data()  {
        username = "";
        room_id = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String un) {
        username = un;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int un) {
        room_id = un;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String pw) { time_start = pw; }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String pw) {
        time_end = pw;
    }
}
