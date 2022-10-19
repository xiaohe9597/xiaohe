package com.example.demo.service;

import redis.clients.jedis.Jedis;

/**
 * @author MG02004
 * @createTime 2022/10/19 9:32
 * @description
 */
public interface RedisService {

    Jedis getResource();

    /**
     * 关闭连接
     * @param jedis
     */
    void returnResource(Jedis jedis);

    void set(String key, String value);

    Object get(String key);
}
