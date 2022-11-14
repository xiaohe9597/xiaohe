package com.example.demo.util;

/**
 * 递归深度
 * 设置堆栈大小，抛出 Exception in thread "main" java.lang.StackOverflowError
 * RUN/DEBUG -> Edit Configurations -> VM options
 * -Xss200k
 * https://blog.csdn.net/minkeyto/article/details/81489041/
 * @author MG02004
 * @createTime 2022/11/3 16:32
 * @description
 */
public class RecursiveUtil {

    static int fn(int n) {
        if (n == 1) {
            return 1;
        }
        return fn(n - 1) + 1;
    }

    static void consolog(String string){
        sout(string);
    }

    private static String sout(String string) {
        System.out.println(string);
        return string;
    }

    public static void main(String[] args) {

        // System.out.println(fn(1000));
        for (int i = 0; i <= 100; ++i) {
            System.out.println(i);
            consolog("xiaohe");

        }
    }
}
