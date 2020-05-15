package cn.flink.jack.kafkasource_transformation;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

// max 和 maxBy 之间的区别在于 max 返回流中的最大值，
// 但 maxBy 返回具有最大值的键， min 和 minBy 同理。

@SuppressWarnings("deprecation")
public class Main9Aggregations {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String> dataStream = env.fromElements("can you help me see can you help me you you");

        SingleOutputStreamOperator<WordWithCount> result = dataStream.flatMap(new FlatMapFunction<String, WordWithCount>() {
            @Override
            public void flatMap(String value, Collector<WordWithCount> out) throws Exception {
                for (String word : value.split(" ")) {
                    out.collect(new WordWithCount(word, 1));
                }
            }
        }).keyBy("word").sum("count");
        result.print().setParallelism(1);

        env.execute("aggregation");
    }

    public static class WordWithCount {
        public String word;
        public Integer count;

        public WordWithCount() {
        }

        public WordWithCount(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " :: " + count;
        }
    }
}
