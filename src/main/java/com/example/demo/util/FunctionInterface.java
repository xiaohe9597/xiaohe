package com.example.demo.util;

/**
 * @author MG02004
 * @createTime 2022/8/12 11:41
 * @description
 */
@FunctionalInterface
public interface FunctionInterface<T> {

    public T getValue(T str);
}
