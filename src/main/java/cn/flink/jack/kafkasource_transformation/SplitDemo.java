package cn.flink.jack.kafkasource_transformation;

import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class SplitDemo {
    public static void main(String[] args) throws Exception {
        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // get input data by connecting to the socket
        DataStreamSource<String> text = env.socketTextStream("192.168.43.131", 9000, "\n");

        SplitStream<String> split = text.split(new OutputSelector<String>() {
            @Override
            public Iterable<String> select(String s) {
                List<String> output = new ArrayList<>();
                if ("a".equals(s)) {
                    output.add("a");
                } else {
                    output.add("其他");
                }
                return output;
            }
        });
        DataStream<String> a = split.select("a");
        DataStream<String> other = split.select("其他");
        DataStream<String> all = split.select("a","其他");
        a.print();
        other.printToErr();
        all.print();

        env.execute();
    }

}
