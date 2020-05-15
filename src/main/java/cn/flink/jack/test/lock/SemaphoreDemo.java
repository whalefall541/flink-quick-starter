package cn.flink.jack.test.lock;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore= new Semaphore(5);
        for (int i = 0; i < 8; i++) {
            new Thread(new Table(semaphore)).start();
        }
    }
}

class Table implements Runnable {
    private Semaphore semaphore;

    public Table(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            // 减少一个信号
            semaphore.acquire();
            System.out.println("过来一波客人,占用一张桌子");
            Thread.sleep((long)(Math.random()*1000));
            System.out.println("客人消费完成");
            // 增加一个信号
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
