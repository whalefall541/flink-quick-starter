package cn.flink.jack.test;

public class Test2 {
    static int[] arr = {3,5,6,0,-10,99};
    public static void main(String[] args) {
//        bubble();
//        insert();
        select();
    }

    /**
     * 选择排序
     */
    private static void select() {
        for (int i = 0; i < arr.length; i++) {
            int min_index = i;
            for (int j = i+1; j < arr.length; j++) {
                if (arr[j]< arr[i])
                    min_index = j;
            }
            int x = arr[i];
            arr[i] = arr[min_index];
            arr[min_index] = x;
        }
        for (int i : arr) {
            System.out.print(i+"\t");
        }
    }

    /**
     * 插入排序
     */
    private static void insert() {
        for (int i = 1; i < arr.length; i++) {
            // 记录要插入元素的值
            int tmp = arr[i];
            // 记录空位置的角标
            int post = i;
            for (int j = i-1; j >= 0 && tmp<arr[j]; j--) {
                arr[i] =arr[j];
                post = j;
            }
            arr[post] = tmp;
        }
        for (int i : arr) {
            System.out.print(i+"\t");
        }
    }


    /**
     * 冒泡
     *  注意别掉了等号 j >= i+1
     */
    public static void bubble(){
        for (int i = 0; i < arr.length; i++) {
            for (int j = arr.length-1; j >= i+1 ; j--) {
                if (arr[j] < arr[j-1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = tmp;
                }
            }
        }
        for (int i : arr) {
            System.out.print(i+"\t");
        }
    }
}
