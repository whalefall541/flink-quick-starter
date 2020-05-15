package cn.flink.jack.mysqlsource;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Main2 {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(new SourceFromMySQL()).print();

        env.execute("Flink add data sourc");
    }
}
