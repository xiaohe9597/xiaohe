package com.example.demo.sort;

/**
 * @author MG02004
 * @createTime 2022/8/3 10:18
 * @description
 */
public class Sortpractise {

    public static void main(String[] args) {
        int[] ints = {2, 7, 3, 33, 5, 70, 6, 7, 9, 24};
        int[] in1 = sortBubble(ints);
        for (int i : in1) {
            System.out.println(i);
        }
    }

    public static int[] sortBubble(int[] ints) { //冒泡排序
        if (ints.length <= 1) {
            return ints;
        }
        for (int i = 0; i < ints.length - 1; i++) {
            for (int j = 0; j < ints.length - 1 - i; j++) {
               if(ints[j] > ints[j+1]){
                   int tmp = ints[j];
                   ints[j] = ints[j+1];
                   ints[j+1] = tmp;
               }
            }
        }
        return ints;
    }

}
