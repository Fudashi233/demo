package cn.edu.jxau.guardedsuspension;

import cn.edu.jxau.util.CodeUtils;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/8
 * Time:上午8:39
 */
public class Server implements Runnable {

    private RequestQueue requestQueue;

    public Server(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {

        while (true) {
            Request request = requestQueue.getRequest();
            CodeUtils.sleep(500);
            System.out.println("Server handle a request:" + request.getName());
        }
    }
}
