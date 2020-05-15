package cn.flink.jack.api;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TupleGenericDemo {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.fromElements(1, 2, 3)
                .map(i -> Tuple2.of(i, i))
                // 1. 显式的指定返回的类型
                // .returns(Types.TUPLE(Types.INT,Types.INT))
                .print();
        
        // 使用匿名类来替代
        /*env.fromElements(1, 2, 3)
                .map(new MapFunction<Integer, Tuple2<Integer, Integer>> (){
            @Override
            public Tuple2<Integer, Integer> map(Integer i) {
                return Tuple2.of(i, i);
            }
        })
        .print();*/

        // 使用Tuple子类来替代 some error in this method
        /*env.fromElements(1, 2, 3)
                .map(i -> new DoubleTuple(i, i))
                .print();*/

        env.execute();
    }

    // Caused by: java.lang.RuntimeException: Cannot instantiate tuple.
    private static class DoubleTuple extends Tuple2<Integer, Integer>{
        public DoubleTuple(int f0, int f1) {
            this.f0 = f0;
            this.f1 = f1;
        }
    }
}
