package com.example.demo.service;

import org.redisson.api.RLock;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * @author MG02004
 * @createTime 2022/10/19 9:32
 * @description
 */
public interface RedisService {

    Jedis getResource();

    /**
     * 关闭连接
     *
     * @param jedis
     */
    void returnResource(Jedis jedis);

    void set(String key, String value);

    Object get(String key);

    void lock(String key, long lockTime, TimeUnit timeUnit, boolean fair);

    void unLock(String key, boolean fair);

    Boolean tryLock(String key, long waitTime, long lockTime, TimeUnit timeUnit, boolean fair) throws Exception;
}
