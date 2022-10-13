package com.example.demo.annotation;

import com.example.demo.eunm.StrategyTypeEnum;

import java.lang.annotation.*;

/**
 * 自定义方法注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface DataDictionary {

}
