package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author MG02004
 * @createTime 2022/8/31 14:39
 * @description
 */
@ControllerAdvice
@Slf4j
public class GlobalHandler {

    @ExceptionHandler(Exception.class)
    public void globalExceptionHandler(Exception e) {
        log.info("全局异常捕获" + e);
        System.out.println("全局异常捕获！");
    }

    @ExceptionHandler(ArithmeticException.class)
    public void arithmeticExceptionHandler(ArithmeticException e) {
        System.out.println("除零异常捕获！");
    }

}
