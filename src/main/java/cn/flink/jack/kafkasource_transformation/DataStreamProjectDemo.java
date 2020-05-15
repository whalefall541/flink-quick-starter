package cn.flink.jack.kafkasource_transformation;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * DataStream<Tuple4<Integer, Double, String, String>> in = // [...]
 * DataStream<Tuple2<String, String>> out = in.project(3,2);
 */
public class DataStreamProjectDemo {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Tuple4<Integer, Double, String, String>> input = env.addSource(new SourceFunction<Tuple4<Integer, Double, String, String>>() {
            private volatile boolean isRunning = true;
            private final Random random = new Random();
            String[] s = new String[]{"A", "B", "C", "D", "E"};

            @Override
            public void run(SourceContext<Tuple4<Integer, Double, String, String>> ctx) throws Exception {
                while (isRunning) {
                    TimeUnit.SECONDS.sleep(1);
                    ctx.collect(Tuple4.of(random.nextInt(100), (double) random.nextInt(100),
                            s[random.nextInt(s.length)], s[random.nextInt(s.length)]));
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        SingleOutputStreamOperator<Tuple> project = input.project(3, 2);
        project.print();
        env.execute();
    }
}
