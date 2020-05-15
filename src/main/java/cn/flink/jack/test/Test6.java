package cn.flink.jack.test;

public class Test6 {
    public static StringBuffer doSome(StringBuffer buffer){
        buffer = new StringBuffer();
        buffer.append("hello world");
        return buffer;
    }

    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("hello");
        doSome(buffer);
        System.out.println(buffer);
    }
}
