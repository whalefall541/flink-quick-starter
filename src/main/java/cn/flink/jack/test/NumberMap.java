package cn.flink.jack.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NumberMap {
    public static void main(String[] args) {
        Map<String,Integer> map = new HashMap<>();
        map.put("zero", 0);
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        map.put("five", 5);
        map.put("six", 6);
        map.put("seven", 7);
        map.put("eight", 8);
        map.put("nine", 9);
        System.out.print("输入：");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.next();
        int tmp = 0 ;
        System.out.print("输出：");
        for (int i = 0; i < str.length()+1; i++) {
            String s = str.substring(tmp,i);
            if (map.get(s)!=null) {
                System.out.print(map.get(s));
                tmp=i;
            }
        }
    }

}
