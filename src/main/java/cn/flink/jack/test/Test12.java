package cn.flink.jack.test;

public class Test12 {
    void test(int i) {
        System.out.println("i am an int");
    }
    void test(String s) {
        System.out.println("i am a String");
    }

    public static void main(String[] args) {
        Test12 t = new Test12();
        char ch = 'y';
        t.test(ch);
    }
}
