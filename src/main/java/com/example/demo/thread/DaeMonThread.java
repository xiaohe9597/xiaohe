package com.example.demo.thread;

/**
 * 守护进程
 * 创建的线程皆为守护进程
 *
 * @author MG02004
 * @createTime 2023/7/11 10:18
 * @description
 */
public class DaeMonThread {

    public static final int SLEEP_GAP = 500; //每一轮睡眠时长
    public static final int MAX_TURN = 4; //线程执行轮次

    static class NormalThread extends Thread {
        static int threadNo = 1;

        public NormalThread() {
            super("normalThread-" + threadNo);
            threadNo++;
        }

        public void run() {
            for (int i = 0; ; i++) { //这里死循环，保证线程一直运行
                ThreadUtil.sleepMilliSeconds(SLEEP_GAP);
                System.out.println(getName() + ", 守护状态为:" + isDaemon());
            }
        }
    }

    public static void main(String[] args) {
        Thread daemonThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                Thread normalThread = new NormalThread();
                normalThread.start();
            }

        }, "daemonThread");
        daemonThread.setDaemon(true); //设置守护进程
        daemonThread.start();

        //这里只有main线程为非守护进程，如果不停顿一下，用户进程（main线程）结束，jvm线程就会结束，守护进程也会强制停止
        //可以考虑停顿时间长些，才能看出效果
        ThreadUtil.sleepMilliSeconds(SLEEP_GAP * MAX_TURN);
        System.out.println(Thread.currentThread().getName() + "运行结束！");
    }

}
