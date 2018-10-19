package com.entity;

public class data {
    private String username;
    private String room_id;
    private String room_name;
    private String time_start;
    private String time_end;

    public data()  {
        username = "";
        room_id = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String un) {
        username = un;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String un) {
        room_id = un;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String un) {
        room_name = un;
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
