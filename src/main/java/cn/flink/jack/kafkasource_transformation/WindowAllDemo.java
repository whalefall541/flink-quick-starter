package cn.flink.jack.kafkasource_transformation;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.evictors.CountEvictor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
import org.apache.flink.streaming.api.windowing.triggers.PurgingTrigger;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.util.Collector;

import java.util.Collection;

public class WindowAllDemo {
    public static void main(String[] args) throws Exception {
        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // get input data by connecting to the socket
        DataStream<String> text = env.socketTextStream("192.168.43.131", 9000, "\n");

        // parse the data, group it, window it, and aggregate the counts
        KeyedStream<SocketWindowWordCount.WordWithCount, Tuple> word = text

                .flatMap(new FlatMapFunction<String, SocketWindowWordCount.WordWithCount>() {
                    @Override
                    public void flatMap(String value, Collector<SocketWindowWordCount.WordWithCount> out) {
                        for (String word : value.split("\\s")) {
                            out.collect(new SocketWindowWordCount.WordWithCount(word, 1L));
                        }
                    }
                })

                .keyBy("word");
        // slide window
                // args1 windowTime args2 trigger interval
               // .timeWindow(Time.minutes(1), Time.seconds(30)).sum("count").print();
                // tumbling window 以滑动步长为6 ，每九个元素统计一次
        // .countWindow(9,6).sum("count").print();
                // define the window  using CountTrigger and PurgingTrigger
        word.window(GlobalWindows.create()).
                trigger(CountTrigger.of(2)).sum("count").print();
        word.window(GlobalWindows.create()).
                trigger(PurgingTrigger.of(CountTrigger.of(2))).sum("count").printToErr();
        word.window(TumblingEventTimeWindows.of(Time.seconds(30))).evictor(CountEvictor.of(10))
                .trigger(CountTrigger.of(10)).sum("count");

        /*text.flatMap(new FlatMapFunction<String, SocketWindowWordCount.WordWithCount>() {
                    @Override
                    public void flatMap(String value, Collector<SocketWindowWordCount.WordWithCount> out) {
                        for (String word : value.split("\\s")) {
                            out.collect(new SocketWindowWordCount.WordWithCount(word, 1L));
                        }
                    }
                }).windowAll(TumblingEventTimeWindows.of(Time.seconds(10))).sum("count").print();*/

        env.execute("Socket Window WordCount");
    }

    /**
     * Data type for words with count.
     */
    public static class WordWithCount {

        public String word;
        public long count;

        public WordWithCount() {}

        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }
    }
}
