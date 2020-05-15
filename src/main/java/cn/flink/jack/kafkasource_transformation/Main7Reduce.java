package cn.flink.jack.kafkasource_transformation;

import cn.flink.jack.mysqlsource.Student;
import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Properties;

/**
 *  Reduce 返回单个的结果值，
 *  并且 reduce 操作每处理一个元素总是创建一个新值。
 *  常用的方法有 average, sum, min, max, count，
 *  使用 reduce 方法都可实现
 */
public class Main7Reduce {
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
        // 先将数据流进行 keyby 操作，因为执行 reduce 操作只能是 KeyedStream，
        // 然后将 student 对象的 age 做了一个求平均值的操作。
        SingleOutputStreamOperator<Student> reduce = student.keyBy(new KeySelector<Student,Integer>() {

            @Override
            public Integer getKey(Student student) {
                return student.age;
            }
        }).reduce(new ReduceFunction<Student>() {
            @Override
            public Student reduce(Student t1, Student t2) {
                Student student1 = new Student();
                student1.name = t1.name + t2.name;
                student1.id = (t1.id + t2.id) / 2;
                student1.password = t1.password + t2.password;
                student1.age = (t1.age + t2.age) / 2;
                return student1;
            }
        });
        reduce.print();
        env.execute("reduce data");
    }
}
