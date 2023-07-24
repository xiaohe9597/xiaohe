package com.example.demo.service;

import com.example.demo.thread.lock.zookeeper.DistributedLock;
import org.apache.curator.framework.CuratorFramework;

/**
 * @author MG02004
 * @createTime 2023/7/12 17:28
 * @description
 */
public interface CuratorFramworkService {

    CuratorFramework getCuratorFramework();

    DistributedLock createLock(String serviceName, String path);
}
