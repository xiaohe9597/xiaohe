package com.example.demo.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ForkJoinPool;

/**
 * @author MG02004
 * @createTime 2022/10/9 11:57
 * @description
 */
@Slf4j
@Service
public class ForkJoinConfig {

    private ForkJoinPool forkJoinPool;

    @PostConstruct
    public void init() {
        this.forkJoinPool = new ForkJoinPool();
    }

    @PreDestroy
    public void destory() {
        if (forkJoinPool != null) {
            forkJoinPool.shutdown();
            log.info("forkJoinPool shotdown...");
        }
    }

    public ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }
}
