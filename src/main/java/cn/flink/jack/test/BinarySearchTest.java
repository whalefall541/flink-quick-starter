package cn.flink.jack.test;

public class BinarySearchTest {
    public static void main(String[] args) {
        int[] arr = {-32, -10, 0, 7, 8, 9};
        int key = 0;
        // 定义一个索引变量接收查找返回值
        int index = search(arr,key);
        System.out.println(index);
    }

    private static int search(int[] arr, int key) {
        int min = 0;
        int max =arr.length-1;
        while (max >= min) {
            int mid = (max+min)/2;
            if (arr[mid] == key)
                return mid;
            if (arr[mid] > key)
                max = mid - 1;
            else
                min = mid + 1;
        }
        return -1;
    }
}
