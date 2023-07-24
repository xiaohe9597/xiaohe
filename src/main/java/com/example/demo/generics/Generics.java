package com.example.demo.generics;

import java.util.Arrays;

/**
 * 泛型类
 *
 * @author MG02004
 * @createTime 2023/7/24 16:25
 * @description
 */
//此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型
//在实例化泛型类时，必须指定T的具体类型
public class Generics<T> {

    //泛型类变量
    //key这个成员变量的类型为T,T的类型由外部指定
    private T key;

    //泛型构造方法形参key的类型也为T，T的类型由外部指定
    public Generics(T key) {
        this.key = key;
    }

    //这也不是一个泛型方法，这也是一个普通的方法，只不过使用了泛型通配符?
    //同时这也印证了泛型通配符章节所描述的，?是一种类型实参，可以看做为Number等所有类的父类
    public static void showKey(Generics<?> obj) {
        System.out.println("泛型测试key value is " + obj.getKey());
    }

    //泛型构造方法形参key的类型也为T，T的类型由外部指定
    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Generics{" +
                "key=" + key +
                '}';
    }

    public static void main(String[] args) {
        //泛型的类型参数只能是类类型（包括自定义类），不能是简单类型
        //传入的实参类型需与泛型的类型参数类型相同，即为Integer.
        Generics genericsInteger = new Generics(1);
        System.out.println(genericsInteger.getKey());
        //自动识别泛型
        Generics genericsString = new Generics("xiaohe");
        System.out.println(genericsString.getKey());

        showKey(new Generics(0.01));
    }
}
