package cn.flink.jack.test.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {
    static int i = 0;

    public static void main(String[] args) throws Exception {
        // 开启公平策略 Lock lock= new ReentrantLock(true);
        Lock lock = new ReentrantLock();

        new Thread(new Add(lock)).start();
        new Thread(new Add(lock)).start();
        Thread.sleep(2000);
        System.out.println(i);
    }
}

class Add implements Runnable {
    private Lock lock;
    private CountDownLatch cdl;

    public Add(Lock lock) {
        this.lock = lock;
    }

    public Add(CountDownLatch cdl) {
        this.cdl = cdl;
    }

    @Override
    public void run() {
        lock.lock();
        for (int i = 0; i < 10000; i++) {
            LockDemo.i++;
        }
        lock.unlock();
    }
}