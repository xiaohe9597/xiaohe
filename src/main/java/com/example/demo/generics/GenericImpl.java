package com.example.demo.generics;

/**
 * 泛型接口实现类
 *
 * @author MG02004
 * @createTime 2023/7/24 16:34
 * @description
 */

import java.util.Date;
import java.util.Random;

/**
 * 未传入泛型实参时，与泛型类的定义相同，在声明类的时候，需将泛型的声明也一起加到类中
 * 即：class FruitGenerator<T> implements Generator<T>{
 * 如果不声明泛型，如：class FruitGenerator implements Generator<T>，编译器会报错："Unknown class"
 */
public class GenericImpl<T> implements GenericInterface<T> {

    private T date;

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    @Override
    public T next() {
        return null;
    }

    /**
     * 泛型方法
     *
     * @param tClass 传入的泛型实参
     * @return T 返回值为T类型
     * 说明：
     * 1）public 与 返回值中间<T>非常重要，可以理解为声明此方法为泛型方法。
     * 2）只有声明了<T>的方法才是泛型方法，泛型类中的使用了泛型的成员方法并不是泛型方法。
     * 3）<T>表明该方法将使用泛型类型T，此时才可以在方法中使用泛型类型T。
     * 4）与泛型类的定义一样，此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型。
     */
    public static <T> T genericMethod(Class<T> tClass) throws IllegalAccessException, InstantiationException {
        T instance = tClass.newInstance();
        return instance;
    }


    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Object object = genericMethod(Class.forName("com.example.demo.generics.GenericImpl")); //反射方式一种
        GenericImpl generic = (GenericImpl)object;
        generic.setDate(new Date());
        System.out.println(generic.getDate());
    }


    /**
     * 声明一个内部类
     * <p>
     * 传入泛型实参时：
     * 定义一个生产器实现这个接口,虽然我们只创建了一个泛型接口Generator<T>
     * 但是我们可以为T传入无数个实参，形成无数种类型的Generator接口。
     * 在实现类实现泛型接口时，如已将泛型类型传入实参类型，则所有使用泛型的地方都要替换成传入的实参类型
     * 即：Generator<T>，public T next();中的的T都要替换成传入的String类型。
     */
    class GenericTwoImpl implements GenericInterface<String> {

        private String[] fruits = new String[]{"apple", "banana", "pear"};

        @Override
        public String next() {
            Random random = new Random();
            return fruits[random.nextInt(3)];
        }
    }
}
