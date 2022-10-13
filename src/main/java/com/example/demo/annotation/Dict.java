package com.example.demo.annotation;

import com.example.demo.constants.CommonConstants;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Dict {

    String dictNo() default "";

    String dictType() default CommonConstants.DictType.CODENO;

    String suffix() default "Desc";

    String[] tableCode() default {};

}
