package cn.flink.jack.test.lock;

import java.util.concurrent.Exchanger;

public class ExchangerDemo {
    public static void main(String[] args) {

        Exchanger<String> ex = new Exchanger<>();
        new Thread(new Producer(ex)).start();
        new Thread(new Consumer(ex)).start();
    }
}

class Producer implements Runnable {
    public Producer(Exchanger<String> ex) {
        this.ex = ex;
    }

    private Exchanger<String> ex;

    @Override
    public void run() {
        try {

            String msg = ex.exchange("货物");
            System.out.println("生产收到消费者交换的" + msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private Exchanger<String> ex;

    public Consumer(Exchanger<String> ex) {
        this.ex = ex;
    }

    @Override
    public void run() {
        try {
            String msg = ex.exchange("财物");
            System.out.println("消费者收到生产者的" + msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
