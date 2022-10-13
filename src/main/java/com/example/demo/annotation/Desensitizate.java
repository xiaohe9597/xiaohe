package com.example.demo.annotation;

import com.example.demo.eunm.StrategyTypeEnum;

import java.lang.annotation.*;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitizate {

    String strategyType() default StrategyTypeEnum.EnumType.phone;

}
