package com.example.demo.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface DateFormat {

    String oldFormat() default "yyyy/MM/dd hh:mm:ss";

    String newFormat() default "yyyy-MM-dd HH:mm:ss";
}
