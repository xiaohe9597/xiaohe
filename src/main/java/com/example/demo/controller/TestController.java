package com.example.demo.controller;

import com.example.demo.init.ForkJoinConfig;
import com.example.demo.service.PubSystemCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private ForkJoinConfig forkJoinConfig;

    @Autowired
    private PubSystemCodeService pubSystemCodeService;

    @ApiOperation(value = "测试fork线程池", notes = "测试fork线程池")
    @RequestMapping(value = "/testForkJoin", method = RequestMethod.GET)
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
}
