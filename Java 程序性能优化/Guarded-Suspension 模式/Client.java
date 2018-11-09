package cn.edu.jxau.guardedsuspension;

import cn.edu.jxau.util.CodeUtils;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/8
 * Time:上午8:42
 */
public class Client implements Runnable {

    private RequestQueue requestQueue;

    public Client(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }


    @Override
    public void run() {

        for (int i = 0; i < 100; i++) {
            CodeUtils.sleep(300);
            Request request = new Request("client request:" + i);
            System.out.println("Client send a request");
            requestQueue.addRequest(request);
        }

    }
}
