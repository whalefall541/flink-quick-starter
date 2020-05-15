package cn.flink.jack.test;

public class T12 {
    String str = new String("good");
    char[] ch = {'a','b','c'};

    public static void main(String[] args) {
        T12 t1 = new T12();
        t1.change(t1.str,t1.ch);
        System.out.print(t1.str+" and ");
        System.out.print(t1.ch);
    }

    private void change(String str, char[] ch) {
        str = "test ok";
        ch[0] = 'g';
    }
}
