package com.example.demo.clazz;

import lombok.experimental.Builder;

import java.lang.reflect.Field;

/**
 * 静态代码块 > 代码块 > 构造方法
 * <p>
 * 执行顺序：
 * 父类静态代代码块 > 子类静态代码块 > 父类代码块 > 父类构造器 > 子类代码块 > 子类构造器
 *
 * @author MG02004
 * @createTime 2022/9/22 10:10
 * @description
 */
public class Reflect {

    static class Father {

        /**
         * 静态代码块
         */
        static {
            System.out.println("父类静态代码块！");
        }

        /**
         * 代码块
         */ {
            System.out.println("父类代码块！");
        }

        public Father() {
            System.out.println("这是父类构造器！");
        }

        public void showName() {
            System.out.println("Father ...");
        }
    }

    static class Child extends Father {

        /**
         * 静态代码块
         */
        static {
            System.out.println("子类静态代码块！");
        }

        /**
         * 代码块
         */ {
            System.out.println("子类代码块！");
        }

        public Child() {
            super();
            System.out.println("这是子类构造器！");
        }

        @Override
        public void showName() {
            System.out.println("Child ...");
        }
    }


    public void console() {
        Father father = new Child();
        System.out.println("father.getClass() :" + father.getClass());
        System.out.println("Father.class :" + Father.class);
        father.showName();
    }

    public static void main(String[] args) {
        new Reflect().console();
    }
}
