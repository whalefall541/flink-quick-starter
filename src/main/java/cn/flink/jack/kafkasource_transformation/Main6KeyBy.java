package cn.flink.jack.kafkasource_transformation;

import cn.flink.jack.mysqlsource.Student;
import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Properties;

/**
 * KeyBy 在逻辑上是基于
 * key 对流进行分区。在内部，它使用 hash 函数对流进行分区。
 * 它返回 KeyedDataStream 数据流。
 */
public class Main6KeyBy {
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

        KeyedStream<Student, Integer> keyBy = student.keyBy(new KeySelector<Student, Integer>() {
            @Override
            public Integer getKey(Student student) throws Exception {
                return student.age;
            }
        });
        keyBy.print();
        env.execute("KeyBy data");
    }
}
