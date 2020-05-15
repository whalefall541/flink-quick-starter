package cn.flink.jack.kafkasource_transformation;

import cn.flink.jack.mysqlsource.Student;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 往kafka中写数据
 * 可以使用这个main函数进行测试一下
 */
public class KafkaUtils2 {
    public static final String broker_list = "192.168.43.131:9092";
    public static final String topic = "student";  // kafka topic，Flink 程序中需要和这个统一

    public static void writeToKafka() throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", broker_list);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //key 序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //value 序列化
        KafkaProducer producer = new KafkaProducer<String, String>(props);


       Student student = new Student();
       student.setId((int)(Math.random()*1000));
       student.setName("jack");
       student.setPassword("6666");
       student.setAge((int)(Math.random()*50));


        ProducerRecord record = new ProducerRecord<String, String>(topic, null, null, JSON.toJSONString(student));
        producer.send(record);
        System.out.println("发送数据: " + JSON.toJSONString(student));

        producer.flush();
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            Thread.sleep(300);
            writeToKafka();
        }
    }
}
