package cn.flink.jack.test;

import java.util.ArrayList;

public class ThreeExit {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(10);
        list.add(11);
        /*
         * 第二种思路 一直遍历这个数组，每隔2个删除一个元素，直到只剩下一个元素 如果遇到了数组的结尾，那么就跳转到开头
         */
       int i  =0;
       while (list.size()!=1) {
           if (i+1>=list.size())
               i=-1;
           if (i+2 >= list.size())
               i =-2;
           i = i+2;
           list.remove(i);
       }
        System.out.println("最后剩下的元素是"+list.get(0));
    }
}
