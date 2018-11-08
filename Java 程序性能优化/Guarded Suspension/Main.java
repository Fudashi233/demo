package cn.edu.jxau.guardedsuspension;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/8
 * Time:上午8:44
 */
public class Main {

    public static void main(String[] args) {

        RequestQueue requestQueue = new RequestQueue();
        new Thread(new Server(requestQueue)).start();
        new Thread(new Client(requestQueue)).start();
    }
}
