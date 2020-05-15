package cn.flink.jack.test;

public class Test4 {
    public static void main(String[] args) {
        String a = "a";
        String b = "b";
        String c = "a"+"b";
        String d = a+b;
        System.out.println(c == d);
    }
}
