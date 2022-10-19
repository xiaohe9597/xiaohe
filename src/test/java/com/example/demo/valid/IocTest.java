package com.example.demo.valid;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author MG02004
 * @createTime 2022/10/18 16:05
 * @description
 */
public class IocTest {

    @Test
    public void test() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanNames
        ) {
            System.out.println(name);
        }
    }
}
