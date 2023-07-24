package com.example.demo.service.impl;

import com.example.demo.service.CuratorFramworkService;
import com.example.demo.service.LockService;
import com.example.demo.thread.lock.zookeeper.DistributedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MG02004
 * @createTime 2023/7/18 17:09
 * @description
 */
@Service
public class LockServiceImpl implements LockService {

    @Autowired
    private CuratorFramworkService curatorFramworkService;

    /**
     * 函数式接口
     */

    @FunctionalInterface
    private interface invoker<T> {
        T invoke();
    }

    /**
     * 上锁
     * @param invoker
     * @param lockName
     * @param <T>
     * @return
     */
    public <T> T lockInvoke(invoker<T> invoker, String lockName) {
        DistributedLock distributedLock = curatorFramworkService.createLock(LockService.class.getSimpleName().toLowerCase(), lockName);
        distributedLock.lock();
        try {
            return invoker.invoke();
        } finally {
            distributedLock.unlock();
        }
    }

}
