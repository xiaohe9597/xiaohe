package com.example.demo.clazz;

/**
 * 反射方式
 *
 * @author MG02004
 * @createTime 2023/7/24 17:56
 * @description
 */
public class ReflectWay {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        //第一种
        Student student = new Student();
        Class clazz = student.getClass();

        //第二种
        Class clazz2 = Class.forName("com.example.demo.clazz.Student");

        //第三种
        Class clazz3 = Student.class;
        System.out.println(clazz.getDeclaredField("name").getName());
        System.out.println(clazz2.getDeclaredField("age").getName());
        System.out.println(clazz3.getDeclaredField("sex").getName());
    }

}
