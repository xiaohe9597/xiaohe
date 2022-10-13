package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MG02004
 * @createTime 2022/10/12 12:00
 * @description
 */
public class DateUtil {

    private static String format = "yyyy/MM/dd hh:mm:ss";
    private static String format2 = "yyyy-MM-dd HH:mm:ss";

    public static Date parse(String str, String format) throws Exception {
        SimpleDateFormat f = new SimpleDateFormat(format);
        Date date = f.parse(str);
        return date;
    }

    public static String format(Date date, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        String str = f.format(date);
        return str;
    }

}
