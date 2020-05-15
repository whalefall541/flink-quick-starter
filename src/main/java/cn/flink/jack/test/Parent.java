package cn.flink.jack.test;

public class Parent {
    int x = 0;

    public class Runner implements Runnable {
        @Override
        public void run() {
            int current = 0;
            for (int i = 0; i < 4; i++) {
                current = x;
                System.out.println(current + ",");
                x = current + 2;
            }
        }
    }
    
    public static void main(String args[]) {
        new Parent().go();
    }

    private void go() {
        Runnable r1 = new Runner();
        new Thread(r1).start();
        new Thread(r1).start();
    }
}
