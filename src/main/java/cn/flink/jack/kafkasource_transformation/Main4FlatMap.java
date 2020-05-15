package cn.flink.jack.kafkasource_transformation;

import cn.flink.jack.mysqlsource.Student;
import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;

import java.util.Properties;

public class Main4FlatMap {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.43.131:9092");
        props.put("zookeeper.connect", "192.168.43.131:2181");
        props.put("group.id", "student-group1");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  //key 反序列化
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "latest"); //value 反序列化

        SingleOutputStreamOperator<Student> student = env.addSource
                (new FlinkKafkaConsumer011<>(
                        "student",  //kafka topic
                        new SimpleStringSchema(),  // String 序列化
                        props)).setParallelism(1).map(string -> JSON.parseObject(string,Student.class));

        SingleOutputStreamOperator<Student> flatMap = student.flatMap(new FlatMapFunction<Student, Student>() {
            @Override
            public void flatMap(Student value, Collector<Student> out) throws Exception {
                if (value.id % 2 == 0) {
                    out.collect(value);
                }
            }
        });
        flatMap.print();

        env.execute("FlatMap data");
    }
}
