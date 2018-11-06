package cn.edu.jxau.future;

import cn.edu.jxau.util.CodeUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/5
 * Time:上午8:31
 */
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start = System.currentTimeMillis();
        Client client = new Client();
        System.out.println((System.currentTimeMillis() - start) / 1000);
        Data data = client.request("hello");
        System.out.println((System.currentTimeMillis() - start) / 1000);
        System.out.println(data.getResult());
        System.out.println((System.currentTimeMillis() - start) / 1000);

        System.out.println("JDK Future模式");
        Callable<String> callable = () -> {
            CodeUtils.sleep(3000);
            return "echo";
        };
        FutureTask<String> futureTask = new FutureTask<>(callable);
        System.out.println((System.currentTimeMillis() - start) / 1000);
        new Thread(futureTask).start();
        System.out.println((System.currentTimeMillis() - start) / 1000);
        System.out.println(futureTask.get());
        System.out.println((System.currentTimeMillis() - start) / 1000);
    }
}
