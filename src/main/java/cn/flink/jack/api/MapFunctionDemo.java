package cn.flink.jack.api;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MapFunctionDemo {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.fromElements(1, 2, 3)
        // 返回 i 的平方
                .map(i -> i*i)
                .print();
        env.execute();
    }
}
