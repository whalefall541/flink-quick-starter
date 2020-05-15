package cn.flink.jack.test.lock;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {

        CyclicBarrier cb = new CyclicBarrier(6);
        new Thread(new Runner(cb), "1号").start();
        new Thread(new Runner(cb), "2号").start();
        new Thread(new Runner(cb), "3号").start();
        new Thread(new Runner(cb), "4号").start();
        new Thread(new Runner(cb), "5号").start();
        new Thread(new Runner(cb), "6号").start();

    }
}

class Runner implements Runnable {

    private CyclicBarrier cb;

    public Runner(CyclicBarrier cb) {
        this.cb = cb;
    }

    @Override
    public void run() {

        try {
            Thread.sleep((long) (Math.random() * 10000));
            String name = Thread.currentThread().getName();
            System.out.println(name + "到了起跑线~~~");
            // 在计数归零之前，使当前线程陷入阻塞
            // 在阻塞的同时会减少一个计数
            cb.await();
            System.out.println(name + "跑了出去~~~");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}