package com.example.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author MG02004
 * @createTime 2022/10/18 14:37
 * @description
 */
@EnableCaching //开启基于注解的缓存（也可以放在启动类上)
@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig extends CachingConfigurerSupport {

    public static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    private int database;
    private String host;
    private String password;
    private int port;
    private int timeout;

    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = jedisPoolConfig();
//        logger.info("init jedisPoolConfig: max-active:{}, min-idle:{}, max-idle:{}, max-wait:{}", jedisPoolConfig.getMaxTotal(),
//                jedisPoolConfig.getMinIdle(), jedisPoolConfig.getMaxIdle(), jedisPoolConfig.getMaxWaitMillis());
        logger.info("init JedisPool ...");
        logger.info("init JedisPool database：{}, host:{}, password:{}, port :{}, timeout:{} ...", database, host, password, port, timeout);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port);
        return jedisPool;
    }

    // 配置缓存管理器
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(15)) // 单位秒
                // 设置key的序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                // 设置value的序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                // 不缓存null值
                .disableCachingNullValues();

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();

        logger.info("自定义RedisCacheManager加载完成");
        return redisCacheManager;
    }

    /**
     * 引入redissonCient, 必须配置RedisConnectionFactory 或者 @SpringBootApplication(exclude = {RedissonAutoConfiguration.class})
     * 二者择其一或者两者都有
     * 否则stringRedisTemplate.opsForValue().setIfAbsent 返回空指针
     *
     * link : https://www.cnblogs.com/hli-tech/p/14989585.html
     * @return
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        lettuceConnectionFactory.setDatabase(database);
        lettuceConnectionFactory.setHostName(host);
        lettuceConnectionFactory.setPassword(password);
        lettuceConnectionFactory.setPort(port);
        lettuceConnectionFactory.setTimeout(timeout);
        return lettuceConnectionFactory;
    }

    // key键序列化方式
    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    // value值序列化方式
    private GenericJackson2JsonRedisSerializer valueSerializer(){
        return new GenericJackson2JsonRedisSerializer();
        // return  new GenericFastJsonRedisSerializer();
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
