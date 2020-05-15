package cn.flink.jack.api.cn.flink.jack.processfunction;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.Date;

public class ProcessFunctionExample {

    // 1.ValueState内部包含了计数、key和最后修改时间
    // 2.对于每一个输入的记录,ProcessFunction都会增加计数,并且修改时间戳
    // 3.该函数会在事件时间的后续1min调度回调函数
    // 4.然后根据每次回调函数,就去检查回调事件时间戳和保存的时间戳,如果匹配就将数据发出

    private static class StreamDataSource extends RichParallelSourceFunction<Tuple3<String, Long, Long>> {

        private volatile boolean running = true;

        @Override
        public void run(SourceContext<Tuple3<String, Long, Long>> sourceContext) throws Exception {

            Tuple3[] elements = new Tuple3[]{
                    Tuple3.of("a", 1L, 1000000050000L),
                    Tuple3.of("a", 1L, 1000000054000L),
                    Tuple3.of("a", 1L, 1000000079900L),
                    Tuple3.of("a", 1L, 1000000115000L),
                    Tuple3.of("b", 1L, 1000000100000L),
                    Tuple3.of("b", 1L, 1000000108000L)
            };

            int count = 0;
            while (running && count < elements.length) {
                sourceContext.collect(new Tuple3<>((String) elements[count].f0, (Long) elements[count].f1, (Long) elements[count].f2));
                count++;
                Thread.sleep(10000);
            }

        }

        @Override
        public void cancel() {
            running = false;
        }
    }


    /**
     * 存储在状态中的对象
     */
    public static class CountWithTimestamp {
        //单词
        public String key;
        //单词计数
        public long count;
        //最近更新时间
        public long lastModified;

        @Override
        public String toString() {
            return "CountWithTimestamp{" +
                    "key='" + key + '\'' +
                    ", count=" + count +
                    ", lastModified=" + new Date(lastModified) +
                    '}';
        }
    }


    /**
     * ProcessFunction有两个泛型类,一个输入一个输出
     */
    public static class CountWithTimeoutFunction extends ProcessFunction<Tuple2<String, Long>, Tuple2<String, Long>> {

        private ValueState<CountWithTimestamp> state;

        //最先调用
        @Override
        public void open(Configuration parameters) throws Exception {
            //根据上下文获取状态
            state = getRuntimeContext().getState(new ValueStateDescriptor<CountWithTimestamp>("myState", CountWithTimestamp.class));
        }

        @Override
        public void processElement(Tuple2<String, Long> input, Context context, Collector<Tuple2<String, Long>> output) throws Exception {

            CountWithTimestamp current = state.value();
            if (current == null) {
                current = new CountWithTimestamp();
                current.key = input.f0;
            }

            //更新ValueState
            current.count++;
            //这里面的context可以获取时间戳
            //todo 此时这里的时间戳可能为null,如果设置的时间为ProcessingTime
            current.lastModified = context.timestamp();
            System.out.println("元素"+input.f0+"进入事件时间为：" + new Date(current.lastModified));
            state.update(current);

            //注册ProcessTimer,更新一次就会有一个ProcessTimer
            context.timerService().registerEventTimeTimer(current.lastModified + 9000);
            System.out.println("定时触发时间为："+new Date(current.lastModified + 9000));
        }

        //EventTimer被触发后产生的行为
        //todo 这里的timestamp是触发时间
        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple2<String, Long>> out) throws Exception {

            //获取上次时间,与参数中的timestamp相比,如果相差等于60s 就会输出
            CountWithTimestamp res = state.value();
            System.out.println("当前时间为："+new Date(timestamp)+res);
            if (timestamp >= res.lastModified + 9000) {
                System.out.println("定时器被触发："+"当前时间为"+new Date(timestamp)+" 最近修改时间为"+new Date(res.lastModified));
                out.collect(new Tuple2<String, Long>(res.key, res.count));
            }
        }
    }


    //执行主类
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<Tuple2<String, Long>> data = env.addSource(new StreamDataSource()).setParallelism(1)
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, Long, Long>>(Time.milliseconds(0)) {
                    @Override
                    public long extractTimestamp(Tuple3<String, Long, Long> input) {
                        return input.f2;
                    }
                }).map(new MapFunction<Tuple3<String, Long, Long>, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(Tuple3<String, Long, Long> input) throws Exception {
                        return new Tuple2<>(input.f0, input.f1);
                    }
                });

        data.keyBy(0).process(new CountWithTimeoutFunction()).print();

        env.execute();
    }
}
