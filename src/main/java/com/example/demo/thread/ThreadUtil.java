package com.example.demo.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * @author MG02004
 * @createTime 2023/7/11 10:58
 * @description
 */
public class ThreadUtil {

    /**
     * 调用jdk底层LockSupport parkNanos 使线程睡眠
     * @param milliSecond
     */
    public static void sleepMilliSeconds(int milliSecond) {
        LockSupport.parkNanos(milliSecond * 1000L * 1000L);
    }
}
