package cn.flink.jack.test;

public class Test11 {
    static int j =20;

    public void amethod(int x){
        x = x*2;
        j = j*2;
    }

    public static void main(String[] args){
        int i = 10;
        Test11 t11 = new Test11();
        t11.amethod(i);
        System.out.println(i+" and "+j);
    }
}
