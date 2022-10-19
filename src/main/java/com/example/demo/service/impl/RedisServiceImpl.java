package com.example.demo.service.impl;

import com.example.demo.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author MG02004
 * @createTime 2022/10/19 9:32
 * @description
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private JedisPool jedisPool; //jedisPool不属于springboot框架支持的redis类,所以需要自行注入到spring中。通过配置类RedisConfig类注入的

    @Override
    public Jedis getResource() {
        return jedisPool.getResource();
    }

    @Override
    public void returnResource(Jedis jedis) {
        //自Jedis3.0版本后jedisPool.returnResource(final Jedis resource)遭弃用,官方重写了Jedis的close()方法用以代替
        if (jedis != null) {
            jedis.close();
            logger.info("jedis close end...");
        }
    }

    @Override
    public void set(String key, String value) {
        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
            logger.info("jedis success set key:{} , value:{}", key, value);
        } catch (Exception e) {
            logger.info("jedis set key fail :{}", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Object get(String key) {
        Jedis jedis = getResource();
        String value = null;
        try {
            value = (String) jedis.get(key);
        } catch (Exception e) {
            logger.info("jedis get value fail :{}", e.getMessage());
        } finally {
            if (jedis != null) {
                logger.info("jedis start close ...");
                jedis.close();
            }
        }
        return value;
    }

}
