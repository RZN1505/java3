package ru.gb.javathree.lesson6;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static int[] fourMethod(int[] arr) {
        List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());
        Collections.reverse(list);
            int pos = list.indexOf(4);
            if (pos < 0 )  throw new RuntimeException();
            List<Integer> sub = list.subList(0, pos);
            Collections.reverse(sub);
            int[] resArray = sub.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
            return resArray;

    }

    public static boolean boolMethod(int[] arr) {
        List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());
        boolean pos = list.indexOf(4) >= 0 || list.indexOf(1)>= 0;
        return pos;

    }
    public static void main(String[] args) {
    }
}