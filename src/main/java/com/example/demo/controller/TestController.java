package com.example.demo.controller;

import com.example.demo.bean.PubSystemCode;
import com.example.demo.eunm.ResultEnum;
import com.example.demo.init.ForkJoinConfig;
import com.example.demo.response.Result;
import com.example.demo.service.PubSystemCodeService;
import com.example.demo.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author MG02004
 * @createTime 2022/10/11 17:02
 * @description
 */
@RequestMapping(value = "test")
@RestController
@Slf4j
@Api(value = "测试Api", description = "测试Api")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private ForkJoinConfig forkJoinConfig;

    @Autowired
    private PubSystemCodeService pubSystemCodeService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "测试fork线程池", notes = "测试fork线程池")
    @RequestMapping(value = "/forkJoin", method = RequestMethod.GET)
    public void testForkJoin() {
        long start1 = System.currentTimeMillis();
//        for (int i=0; i<=10000; i++) {
//            pubSystemCodeService.selectValueByCodeType("ASSETR_TYPE", String.valueOf(i));
//        }
        forkJoinConfig.getForkJoinPool().submit(() ->
                {
                    for (int i = 0; i <= 10000; i++) {
                        System.out.println(i);
                        pubSystemCodeService.selectValueByCodeType("ASSETR_TYPE", String.valueOf(i));
                    }
                }
        ).join(); //join() 等待线程池执行完毕，继续后续代码块，类似同步作用；去掉join()，程序异步执行
        long end1 = System.currentTimeMillis();
        log.info("0-10000000 耗时{}秒", (end1 - start1) / 1000);
    }

    @ApiOperation(value = "测试jedis set值", notes = "测试jedis set值")
    @RequestMapping(value = "/jedisSetValue", method = RequestMethod.GET)
    public void jedisSetValue(String key, String value) {
        redisService.set(key, value);
        boolean exists = stringRedisTemplate.hasKey(key);
        logger.info("stringRedisTemplate hasKey: {}", exists);
        logger.info("stringRedisTemplate delete: {}", stringRedisTemplate.delete(key));
        stringRedisTemplate.opsForValue().set("xh", "wTing");

    }

    @ApiOperation(value = "测试jedis get value值", notes = "测试jedis get value值")
    @RequestMapping(value = "/jedisGetValue", method = RequestMethod.GET)
    public Result jedisGetValue(String key) {
        String value = (String) redisService.get(key);
        return Result.BuildResponseResult(ResultEnum.SUCESS.getCode(), ResultEnum.SUCESS.getMsg(), value);
    }

    @ApiOperation(value = "测试事务传播行为", notes = "测试事务传播行为")
    @RequestMapping(value = "/transactionPropagation", method = RequestMethod.POST)
    public void transactionPropagationTest(@RequestBody PubSystemCode pubSystemCode) {
        pubSystemCodeService.addPubSystemCode(pubSystemCode);
    }

    @ApiOperation(value = "模拟库存", notes = "模拟库存")
    @RequestMapping(value = "/simulate_stock", method = RequestMethod.POST)
    public void simulateStock() {
        //首先，设置库存
        stringRedisTemplate.opsForValue().setIfAbsent("stock", "100");
        //上锁
        String key = "simulate_stock";
        try {
            redisService.lock(key, 0, TimeUnit.SECONDS, false);
            //业务减库存
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                stock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
                logger.info("扣减库存成功！");
            } else
                logger.info("扣减库存失败！");
        } finally {
            //解锁
            redisService.unLock(key, false);
        }
    }

    @ApiOperation(value = "试图模拟库存", notes = "试图模拟库存")
    @RequestMapping(value = "/try_simulate_stock", method = RequestMethod.POST)
    public void trySimulateStock() {
        //首先，设置库存
        stringRedisTemplate.opsForValue().setIfAbsent("stock", "100");
        //上锁
        String key = "simulate_stock";
        try {
            System.out.println("simulate_stock start：" + System.currentTimeMillis());
            Boolean bolean = redisService.tryLock(key, 5, -1, TimeUnit.SECONDS, false);
            //获取锁成功
            if (bolean) {
                System.out.println("simulate_stock end：" + System.currentTimeMillis() + ", 获取锁成功" + bolean);
                //业务减库存
                int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
                if (stock > 0) {
                    stock = stock - 1;
                    stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
                    logger.info("扣减库存成功！");
                } else
                    logger.info("扣减库存失败！");
            }
        } catch (Exception e) {
            System.out.println("simulate_stock end2：" + System.currentTimeMillis());
            e.printStackTrace();
        } finally {
            //解锁
            redisService.unLock(key, false);
        }
    }

    static class ObjectLock {
        //对象锁：普通方法
        public synchronized void lock() throws InterruptedException {
//延时1s执行日志输出
            TimeUnit.SECONDS.sleep(1);
            System.out.println("lock1 executeTime = " + System.currentTimeMillis());
        }

        //对象锁：普通方法代码块
        public void lock2() throws InterruptedException {
            synchronized (this){
                //延时1s执行日志输出
                TimeUnit.SECONDS.sleep(1);
                System.out.println("lock2 executeTime = " + System.currentTimeMillis());
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {

        ObjectLock classLock = new ObjectLock();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    classLock.lock();
//          classLock.lock2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    classLock.lock();
//          classLock.lock2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        //由于t1 和 t2 不存在对象锁资源竞争，所以两个线程真正执行时间一样
    }

}
