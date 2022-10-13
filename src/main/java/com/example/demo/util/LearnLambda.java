package com.example.demo.util;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author MG02004
 * @createTime 2022/8/12 11:01
 * @description
 */
public class LearnLambda {

    public static void main(String[] args) throws Throwable {
        //不使用lambda表达式
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        System.out.println(comparator.compare("3", "2"));
        // 使用lambda表达式
        Comparator<Double> comparator1 = (x, y) -> x.compareTo(y);
        System.out.println(comparator1.compare(2.0, 1.0));
        System.out.println("------------lambda语法---------------------------------");

        //函数式接口lambda写法
        String str = "abc";
        FunctionInterface<String> functionInterface = s -> s.toUpperCase();
        System.out.println(upper(functionInterface, str));
        System.out.println("------------函数式接口lambda写法------------------------");

        //java 内置四大核心接口Comsumer<T>
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("this is Comsumer's accpet method！");
            }
        };
        consumer.accept("comsumer");
        System.out.println("------------Comsumer<T>-------------------------------");

        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                System.out.println("this is Supplier's get method！");
                return "111";
            }
        };
        System.out.println(supplier.get());
        System.out.println("------------Supplier<T>------------------------------");

        Function<String, String> function = new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s;
            }
        };
        System.out.println(function.apply("周五了！"));
        System.out.println("------------Function<T, R>------------------------------");

        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return "yes".equalsIgnoreCase(s);
            }
        };
        System.out.println(predicate.test("YES"));
        System.out.println("------------Predicate<T>------------------------------");

        //用predicate过滤字符串
        Predicate<String> predicate1 = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.contains("京");
            }
        };
        List<String> list = Arrays.asList("北京", "天京", "南京", "东京", "湖京", "武汉", "游京");
        List<String> sts = filterString(list, predicate1);
        for (String st : sts) {
            System.out.println(st);
        }
        System.out.println("----------------------------------------");
        List<String> sts2 = filterString(list, s -> s.contains("汉"));
        for (String st : sts2) {
            System.out.println(st);
        }
        System.out.println("----------------------------------------");

        //方法引用
        PrintStream ps = System.out;
        Consumer<String> consumer1 = ps::println;
        consumer1.accept("add");
        System.out.println("----------------------------------------");

        Function<Double, Long> function1 = new Function<Double, Long>() {
            @Override
            public Long apply(Double aDouble) {
                return Math.round(aDouble);
            }
        };
        System.out.println(function1.apply(1.01));
        System.out.println("----------------------------------------");

        Function<Double, Long> function2 = s -> Math.round(s);
        System.out.println(function2.apply(2.23));

        Function<Double, Long> function3 = LearnLambda::getLong;
        System.out.println(function3.apply(0.22));
        System.out.println("----------------------------------------");

        Comparator<String> comparator2 = (a1, a2) -> a1.compareTo(a2);
        System.out.println(comparator2.compare("a1", "s2"));
        Comparator<String> comparator3 = String::compareTo;
        System.out.println(comparator3.compare("s2", "a1"));
        System.out.println("----------------------------------------");

        //构造器引用
        Supplier<Student> supplier1 = Student::new;
        //System.out.println(supplier1.get());
        Function<Integer, String[]> function4 = String[]::new;
        String[] sts4 = function4.apply(12);
        System.out.println(sts4.length);
        System.out.println("----------------------------------------");

        //Stream API
        //创建流
//        List<String> strings = Arrays.asList("001", "002", "003", "004");
////        Stream<String> st = strings.stream(); //顺序流
////        Stream<String> st2 = strings.parallelStream(); //并行流
////        IntStream intStream = Arrays.stream(new int[]{1, 2}); //通过数组
////        Stream<Integer> intStream1 = Stream.of(1, 2, 3, 4); //Stream.of
////        Stream.iterate(0, t -> t + 2).limit(10).forEach(System.out::println);//迭代
////        Stream.generate(Math::random).limit(10).forEach(System.out::println); //生成
        //Stream 集合api
        List<String> lts = Arrays.asList("张三", "李四", "王五", "赵六", "张龙", "赵虎", "王朝", "马汉", "赵虎");
        lts.stream().filter(o -> o.contains("赵")).collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("----------------------------------------");
        lts.stream().distinct().collect(Collectors.toList()).forEach(System.out::println);//去重
        System.out.println("----------------------------------------");
        //lts.stream().limit(2).collect(Collectors.toList()).forEach(System.out::println);//截取
        lts.stream().skip(2).collect(Collectors.toList()).forEach(System.out::println); //跳过
        System.out.println("----------------------------------------");
        lts.stream().map(o -> o.concat("01")).collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("----------------------------------------");
        List<String> its = Arrays.asList("5", "3", "1", "2", "4");
        its.stream().mapToDouble(o -> Double.parseDouble(o)).forEach(System.out::println);
        System.out.println("----------------------------------------");
        its.stream().mapToInt(o -> Integer.parseInt(o)).forEach(System.out::println);
        System.out.println("----------------------------------------");
        its.stream().mapToLong(o -> Long.valueOf(o)).forEach(System.out::println);
        System.out.println("-----------------22-----------------------");
        its.stream().flatMap(o -> Stream.of(Integer.parseInt(o))).forEach(System.out::println);
        System.out.println("-----------------22-----------------------");
        its.stream().sorted().forEach(System.out::println);
        System.out.println("-----------------22-----------------------");
        its.stream().sorted((o1, o2) -> o1.compareTo(o2)).forEach(System.out::println);
        System.out.println("-----------------33-----------------------");
        List<String> lts3 = Arrays.asList("李四", "李四", "王五", "赵六", "张龙", "赵虎", "王朝", "马汉", "赵虎");
        System.out.println(lts3.stream().allMatch(o -> o.contains("赵")));
        System.out.println("-----------------33-----------------------");
        System.out.println(lts3.stream().anyMatch(o -> o.contains("赵")));
        System.out.println("-----------------33-----------------------");
        System.out.println(lts3.stream().noneMatch(o -> o.equalsIgnoreCase("jj")));
        System.out.println("-----------------33-----------------------");
        System.out.println(lts3.stream().findFirst() + "--" + lts3.stream().findAny());
        System.out.println(lts3.stream().findAny());
        System.out.println("-----------------33-----------------------");
        System.out.println(lts3.stream().count());
        List<Integer> its4 = Arrays.asList(1, 2, 3, 4, 545, 6, 9);
        System.out.println(its4.stream().max(Integer::compare).get());
        System.out.println(its4.stream().min(Integer::compare).get());
        System.out.println("-----------------44-----------------------");
        System.out.println(its4.stream().reduce(0, (x, y)->x+y));
        System.out.println(its4.stream().reduce(Integer::sum).get());
        System.out.println("-----------------55-----------------------");
        //Optional
        Optional<String> optional = Optional.of("sts");
        System.out.println(optional.get());
        System.out.println("-----------------55-----------------------");
        Optional<String> optional1 = Optional.empty();
        System.out.println(optional1.isPresent());
        Optional<String> optional3 = Optional.ofNullable("3");
        optional3.ifPresent(s -> System.out.println(s));
        System.out.println(optional1.orElse("003"));
        //System.out.println(optional3.get());
        System.out.println(optional1.orElseGet(new Supplier<String>() {
            @Override
            public String get() {
                return "null";
            }
        }));
        System.out.println(optional1.orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return new Exception("orElseThrow");
            }
        }));
    }


    private static Long getLong(Double aDouble) {
        return 1l;
    }


    public static String upper(FunctionInterface<String> functionInterface, String string) {
        return functionInterface.getValue(string);
    }

    /**
     * 根据给定规则，过滤集合中字符串，此规则由predicate的方法来定义
     */
    public static List<String> filterString(List<String> list, Predicate<String> predicate) {
        List<String> arrayList = new ArrayList<>();
        for (String string : list) {
            if (predicate.test(string)) {
                arrayList.add(string);
            }
        }
        return arrayList;
    }

}
