package cn.flink.jack.test;

public class InsertSortTest {
    public static void main(String[] args) {
        int[] arr = {1,-10,-32,0,8,32,19};
        insert(arr);
    }

    private static void insert(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int tmp = arr[i];
            int pos = i;
            for (int j =i-1; j >=0 && tmp < arr[j] ; j--) {
                arr[j+1] =arr[j];
                pos = j;
            }
            arr[pos] = tmp;
        }
        for (int i : arr) {
            System.out.print(i+"\t");
        }
    }
}
