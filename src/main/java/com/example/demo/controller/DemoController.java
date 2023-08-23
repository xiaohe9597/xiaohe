package com.example.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MG02004
 * @createTime 2022/8/31 14:33
 * @description
 */
@Api(value = "异常", description = "异常")
@RestController
@RequestMapping(value = "/exception")
public class DemoController {

    @ApiOperation(value = "arithmeticException", notes = "arithmeticException")
    @RequestMapping(value = "/arithmeticException", method = RequestMethod.GET)
    public String demo1() {
        int i = 100 / 0; // ArithmeticException
        System.out.println("console erorr");
        return "arithmeticException";
    }

    @ApiOperation(value = "nullPointException", notes = "nullPointException")
    @RequestMapping(value = "nullPointException", method = RequestMethod.GET)
    public String nullPointException() {
//        String str = null;
//        System.out.println(str.equalsIgnoreCase("xiaohe"));
        return "nullPointException";
    }
}
