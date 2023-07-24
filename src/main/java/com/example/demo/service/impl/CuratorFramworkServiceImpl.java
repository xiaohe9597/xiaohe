package com.example.demo.service.impl;

import com.example.demo.config.AppConfig;
import com.example.demo.service.CuratorFramworkService;
import com.example.demo.thread.lock.zookeeper.DistributedLock;
import com.example.demo.thread.lock.zookeeper.ZookeeperPathUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author MG02004
 * @createTime 2023/7/12 17:28
 * @description
 */
@Service
public class CuratorFramworkServiceImpl implements CuratorFramworkService {

    @Autowired
    private AppConfig appConfig;

    private CuratorFramework curatorFramework;

    @PostConstruct
    public void init() {
        String connectString = appConfig.getStr(AppConfig.ZOOKEEPER_ADDRESS);
        curatorFramework = newCuratorFramework(connectString, appConfig.getAppName(), new RetryPolicy() {
            @Override
            public boolean allowRetry(int retryCount, long elapsedTimeMs, RetrySleeper sleeper) {
                return false;
            }
        });
    }

    private CuratorFramework newCuratorFramework(String connectString, String appName, RetryPolicy retryPolicy) {
        CuratorFramework curatorFramework = null;
        curatorFramework = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        curatorFramework.start();
        try {
            if (!curatorFramework.blockUntilConnected(10000, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException(String.format("connect zooKeeper[%s] failed", connectString));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(String.format("connect zooKeeper[%s] exception", connectString), e);
        }
        String rootPath = ZookeeperPathUtils.getRootPath(appName);
        /** 初始化应用基本目录 */
        try {
            Stat stat = curatorFramework.checkExists().forPath(rootPath);
            if(null == stat){
                curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(rootPath);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("init root's path[%s] of zooKeeper of application exception", rootPath), e);
        }
        return curatorFramework;
    }

    @PreDestroy
    public void destroy() {
        if (curatorFramework != null) {
            curatorFramework.close();
        }
    }

    @Override
    public CuratorFramework getCuratorFramework() {
        return this.curatorFramework;
    }

    @Override
    public DistributedLock createLock(String serviceName, String path) {
        if (StringUtils.isBlank(path)) {
            throw new RuntimeException(String.format("the path must not be blank!"));
        }
        DistributedLock distributedLock = null;
        path = ZookeeperPathUtils.getSubPath(appConfig.getAppName(), serviceName, path); //封装path
        InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(curatorFramework, path);
        distributedLock = new ZookeeperDistributeLock(path, interProcessReadWriteLock);
        return distributedLock;
    }

    static class ZookeeperDistributeLock implements DistributedLock {

        private String path;
        private InterProcessReadWriteLock interProcessReadWriteLock;

        public ZookeeperDistributeLock(String path, InterProcessReadWriteLock interProcessReadWriteLock) {
            this.path = path;
            this.interProcessReadWriteLock = interProcessReadWriteLock;
        }

        @Override
        public void lock() {
            try {
                interProcessReadWriteLock.writeLock().acquire();
            } catch (Exception e) {
                throw new RuntimeException(String.format("ZookeeperDistributeLock[%s] lock path exception", path), e);
            }
        }

        @Override
        public void lock(long time) {
            try {
                interProcessReadWriteLock.writeLock().acquire(time, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                throw new RuntimeException(String.format("ZookeeperDistributeLock[%s] lock path exception", path), e);
            }
        }

        @Override
        public void unlock() {
            try {
                interProcessReadWriteLock.writeLock().release();
            } catch (Exception e) {
                throw new RuntimeException(String.format("ZookeeperDistributeLock[%s] unlock path exception", path), e);
            }
        }
    }
}
