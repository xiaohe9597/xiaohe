package com.example.demo.service.impl;

import com.example.demo.service.RedisService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @author MG02004
 * @createTime 2022/10/19 9:32
 * @description
 */
@Service
public class RedisServiceImpl<psvm> implements RedisService {

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

    @Resource
    private RedissonClient redissonClient;

    @Value("${redisson.lock.prefix}")
    private String prefix;

    /**
     * 加锁
     *
     * @param key      保证业务唯一性
     * @param lockTime 锁定时长
     * @param timeUnit 时间单位
     * @param fair     公平锁
     * @return
     */
    @Override
    public void lock(String key, long lockTime, TimeUnit timeUnit, boolean fair) {
        RLock rLock = getLock(key, fair);
        boolean bolean;
        if (lockTime > 0) {
            rLock.lock(lockTime, timeUnit);
        } else
            //lockTime < 0，未设置过期时间，默认看门狗机制
            //自动延期机制 默认续30s 每隔30/3=10 秒续到30s
            rLock.lock();
    }

    /**
     * @param key
     * @param waitTime 等待获取锁的时间
     * @param lockTime
     * @param timeUnit
     * @param fair
     * @return
     * @throws Exception
     */
    @Override
    public Boolean tryLock(String key, long waitTime, long lockTime, TimeUnit timeUnit, boolean fair) throws Exception {
        RLock rLock = getLock(key, fair);
        boolean bolean;
        if (lockTime > 0) {
            bolean = rLock.tryLock(waitTime, lockTime, timeUnit);
        } else
            //lockTime < 0，未设置过期时间，默认看门狗机制
            //自动延期机制 默认续30s 每隔30/3=10 秒续到30s
            bolean = rLock.tryLock(waitTime, timeUnit);
        return bolean;
    }

    /**
     * 解锁
     *
     * @param key
     * @param fair
     */
    @Override
    public void unLock(String key, boolean fair) {
        RLock rLock = getLock(key, fair);
        if (rLock.isLocked()) {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                logger.info("释放分布式锁异常", e);
            }
        }
    }

    private RLock getLock(String key, boolean fair) {
        RLock rLock;
        if (fair) {
            rLock = redissonClient.getFairLock(prefix + ":" + key);
        } else
            rLock = redissonClient.getLock(prefix + ":" + key);
        Assert.notNull(rLock, "Redisson Lock acquisition failure！");
        return rLock;
    }

    static class Thread1 extends Thread {

        @Override
        public void run() {
            try {
                System.out.println("Thread1 run！！！");
                LockSupport.park(this);
            } finally {
                System.out.println("线程阻塞了！还会进finally吗？");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        Thread1 thread1 = new Thread1();
//        thread1.start();
//        //main线程去唤醒
//
//        Thread.sleep(3000l);
//        System.out.println("main 去唤醒！");
//        System.out.println("Thread1 isAlive:" + thread1.isAlive());
//        Thread.sleep(1000l);
//        thread1.interrupt(); //打断线程阻塞
//
//        System.out.println("thread1 isInterrupted:" + thread1.isInterrupted());
        //LockSupport.unpark(thread1);
        final String i = null;
        System.out.println(i);

    }

}
