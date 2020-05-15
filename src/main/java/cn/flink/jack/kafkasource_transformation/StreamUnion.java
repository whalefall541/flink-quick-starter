package cn.flink.jack.kafkasource_transformation;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import javax.annotation.Nullable;

/**
 * have no result
 */
public class StreamUnion {
    public static void main(String[] args) throws Exception {
        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // get input data by connecting to the socket
        DataStream<String> text = env.socketTextStream("192.168.43.131", 9000, "\n").assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<String>() {
            @Nullable
            @Override
            public Watermark getCurrentWatermark() {
                return null;
            }

            @Override
            public long extractTimestamp(String s, long l) {
                return 0;
            }
        });

        // text.union(text,text).printToErr();
        text.join(text).where(s -> s).equalTo(s -> s).window(TumblingEventTimeWindows.of(Time.seconds(10))).apply((s, s2) -> s2).print();
        env.execute();
    }
}
