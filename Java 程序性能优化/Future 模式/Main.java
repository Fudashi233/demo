package cn.edu.jxau.future;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/5
 * Time:上午8:31
 */
public class Main {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        Client client = new Client();
        System.out.println((System.currentTimeMillis() - start) / 1000);
        Data data = client.request("hello");
        System.out.println((System.currentTimeMillis() - start) / 1000);
        System.out.println(data.getResult());
        System.out.println((System.currentTimeMillis() - start) / 1000);
    }
}
