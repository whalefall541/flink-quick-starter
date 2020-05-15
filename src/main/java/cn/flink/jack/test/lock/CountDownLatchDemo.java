package cn.flink.jack.test.lock;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {

        CountDownLatch cdl = new CountDownLatch(5);
        new Thread(new Teacher(cdl)).start();
        new Thread(new Student(cdl)).start();
        new Thread(new Student(cdl)).start();
        new Thread(new Student(cdl)).start();
        new Thread(new Student(cdl)).start();
        // 在计数归零之前，线程需要一直被阻塞
        cdl.await();
        System.out.println("考试开始~~~");

    }
}


class Teacher implements Runnable {

    private CountDownLatch cdl;

    public Teacher(CountDownLatch cdl) {
        this.cdl = cdl;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("考官到达考场~~~");
        // 减少一个计数
        cdl.countDown();
    }
}

class Student implements Runnable {

    private CountDownLatch cdl;

    public Student(CountDownLatch cdl) {
        this.cdl = cdl;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("考生到达考场~~~");
        cdl.countDown();
    }
}
