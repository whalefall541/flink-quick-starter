package cn.flink.jack.test;

public class CompressString {
    public static String compressStr(String str){
        StringBuilder sb = new StringBuilder();
        int sum =1;
        char c1 = str.charAt(0);
        for (int i = 1; i < str.length(); i++) {
            char c2 = str.charAt(i);
            if (c1 == c2) {
                sum ++;
                continue;
            }
            if (sum >1){
                sb.append(c1).append(sum);
            } else {
                sb.append(c1);
            }
            c1 =c2;
            sum =1;
        }
        if (sum >1){
            sb.append(c1).append(sum);
        } else {
            sb.append(c1);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(CompressString.compressStr("aaabbbc"));
    }
}
