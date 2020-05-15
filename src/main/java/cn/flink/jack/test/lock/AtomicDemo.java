package cn.flink.jack.test.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo {
   static AtomicInteger ai = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch cdl =new CountDownLatch(2);

        new Thread(new Add1(cdl)).start();
        new Thread(new Add1(cdl)).start();
        cdl.await();
        System.out.println("ai = " + ai);
    }

}
class Add1 implements Runnable {

    private CountDownLatch cdl;

    public Add1(CountDownLatch cdl) {
        this.cdl = cdl;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            // AtomicDemo.i++;
            AtomicDemo.ai.incrementAndGet();
        }
        cdl.countDown();
    }
}
