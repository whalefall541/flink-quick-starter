package cn.flink.jack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        String str = null;
        String str1 = "a";

//        System.out.println(str.toString());// NullPointException
        System.out.println(String.valueOf(str));// null
        System.out.println((String) str); // null

    }
}
