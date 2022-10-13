package com.example.demo.aspect;

import com.example.demo.annotation.DataDictionary;
import com.example.demo.util.DataDictionaryUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.weaver.AjAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * @author MG02004
 * @createTime 2022/10/9 10:47
 * @description
 */
@Component
@Aspect
public class DataDictionaryAspect {

    @Autowired
    private DataDictionaryUtil dataDictionaryUtil;

    @Around(value = "@annotation(dataDictionary)")
    public Object doAround(ProceedingJoinPoint joinPoint, DataDictionary dataDictionary) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object object = joinPoint.proceed(args);
        if (object != null) {
            return dataDictionaryUtil.dictionary(object);
        }
        return object;
    }
}
