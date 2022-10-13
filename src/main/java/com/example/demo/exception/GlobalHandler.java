package com.example.demo.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author MG02004
 * @createTime 2022/8/31 14:39
 * @description
 */
@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(Exception.class)
    public void globalExceptionHandler(Exception e){
        System.out.println("全局异常捕获！");
    }

    @ExceptionHandler(ArithmeticException.class)
    public void arithmeticExceptionHandler(ArithmeticException e){
        System.out.println("除零异常捕获！");
    }

}
