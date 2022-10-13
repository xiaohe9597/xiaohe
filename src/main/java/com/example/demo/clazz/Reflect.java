package com.example.demo.clazz;

import lombok.experimental.Builder;

import java.lang.reflect.Field;

/**
 * @author MG02004
 * @createTime 2022/9/22 10:10
 * @description
 */
public class Reflect {

    class Father {
        public void showName() {
            System.out.println("Father ...");
        }
    }

    class Child extends Father {
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
