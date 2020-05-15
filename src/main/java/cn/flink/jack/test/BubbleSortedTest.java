package cn.flink.jack.test;

public class BubbleSortedTest {
    public static void main(String[] args) {
        int[] arr = {1,-10,-32,0,8,32,19};
        bubbleSort(arr);
    }

    private static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = arr.length-1; j >=1 ; j--) {
                if (arr[j] < arr[j-1]) {
                    int tmp = arr[j];
                    arr[j] =arr[j-1];
                    arr[j-1] =tmp;
                }
            }
        }
        for (int x: arr)
            System.out.print(x+"\t");
    }
}
