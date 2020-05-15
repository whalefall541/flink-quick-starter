package cn.flink.jack.test;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

public   class Test1 {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(5);
        Future<String> f = es.submit(new C<String>());
        System.out.println(f.get());
        es.shutdown();
    }
}

class A extends Thread {
    @Override
    public void run() {
        System.out.println("A run");
    }
}
class B implements Runnable{
    @Override
    public void run() {
        System.out.println("true = " + true);
    }
}

class C<v> implements Callable<v> {
    @Override
    public v call() throws Exception {
        System.out.println("callable thread"+Thread.currentThread().getName());
        return null;
    }
}