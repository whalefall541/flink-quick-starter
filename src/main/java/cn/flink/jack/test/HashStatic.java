package cn.flink.jack.test;

public class HashStatic {
    private static int x =100;
    public static void main(String[] args) {
        HashStatic hs1 = new HashStatic();
        hs1.x++;
        HashStatic hs2 = new HashStatic();
        hs2.x++;

        hs1 = new HashStatic();
        hs1.x++;

        HashStatic.x--;
        System.out.println("x="+x);
    }
}
