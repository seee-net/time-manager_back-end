package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormDate {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //phpMyadmine中的Datetime对应Java的Timestamp，我将字符串转为Timestamp
    public static String setFormDate(Date noFormDate) {
        return sdf.format(noFormDate);
    }

    public static String addZero(int time) {
        if (10 >  time)
            return "0" + time;
        else
            return time + "";
    }
}

