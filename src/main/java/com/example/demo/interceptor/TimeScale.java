package com.example.demo.interceptor;

/**
 * @author MG02004
 * @createTime 2022/10/18 10:53
 * @description
 */
public class TimeScale {

    /**
     * 将时长转化为区间描述
     *
     * @param time
     * @return
     */
    public static String getString(int time) {
        if (time == 0 || time < 1) {
            return "[小于1秒]";
        } else if (time > 1 && time <= 5) {
            return "[大于1秒]";
        } else if (time > 5 && time <= 10) {
            return "[大于5秒]";
        } else if (time > 10 && time <= 30) {
            return "[大于10秒]";
        } else if (time > 30 && time <= 60) {
            return "[大于30秒]";
        } else if (time > 60 && time <= 100) {
            return "[大于60秒]";
        } else if (time > 100) {
            return "[大于100秒]";
        }
        return "[大于1秒]";
    }
}
