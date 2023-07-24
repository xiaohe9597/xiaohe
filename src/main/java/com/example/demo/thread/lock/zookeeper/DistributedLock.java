package com.example.demo.thread.lock.zookeeper;

public interface DistributedLock {

    void lock();

    void lock(long time);

    void unlock();


}
