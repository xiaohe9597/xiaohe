package com.example.demo.valid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author MG02004
 * @createTime 2022/9/15 17:25
 * @description
 */
public class ListArray {

    private int v;
    private LinkedList<Integer> vars[] = new LinkedList[6];

    public ListArray(int v, LinkedList<Integer>[] vars) {
        this.v = v;
        vars = new LinkedList[v];
        for (int i = 0; i < v; i++) {

        }
    }

    public static void main(String[] args) {
//        int v = 5;
//        LinkedList<Integer> adj[] = new LinkedList[5];
//        for (int i = 0; i < v; i++) {
//            adj[i] = new LinkedList<>();
//            System.out.println(adj[i].getClass());
//        }
//        System.out.println(adj.length);

        int number = 1000;//假设1000个订单数
        Double[] weight = new Double[]{1D, 1D, 1D, 1D, 1D};
        //已分配订单数
        Integer[] count = new Integer[weight.length];
        for (int i = 0; i < number; i++) {
            //当前权重
            Double[] current = new Double[weight.length];
            for (int w = 0; w < weight.length; w++) {
                current[w] = weight[w] / (count[w] == null ? 1 : count[w]);
            }
            int index = 0;
            Double currentMax = current[0]; // 每次取第一个人
            for (int d = 1; d < current.length; d++) {
                //考虑全等的情况
                Boolean isTrue = true;
                while (isTrue) {
                    Set set = new HashSet();
                    for (Double c : current) {
                        set.add(c);
                    }
                    if (set.size() == 1) {//代表全等
                        for (int e = 0; e < current.length; e++) {
                            current[e] = current[e] * Math.random(); //附上权重
                        }
                    } else {
                        isTrue = false;
                    }
                }
                //比较所有的数,寻找出数值最大的那一位下标
                if (currentMax < current[d]) {
                    currentMax = current[d];
                    index = d;
                }
            }
            count[index] = count[index] == null ? 1 : count[index] + 1; //一次循环分配一个
        }
        for (Integer i : count) {
            System.err.println(i);
        }

    }

}
