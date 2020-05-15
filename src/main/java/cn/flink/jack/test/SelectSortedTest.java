package cn.flink.jack.test;

import java.util.Arrays;

public class SelectSortedTest {
    public static void main(String[] args) {
        int[] arr = {1,-10,-32,0,8,32,19};
//        selectSort(arr);
        Arrays.sort(arr);
        for (int x : arr)
            System.out.println(x);
    }

    private static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            // 记录最小值的角标 开始时假设i角标对应的值最小
            int temp = i;
            for (int j = i+1;j < arr.length;j++) {
                if (arr[j] < arr[temp])
                    temp = j;
            }
            int x = arr[temp];
            arr[temp] = arr[i];
            arr[i] = x;
        }
        for (int y : arr)
            System.out.println(y);
    }
}
