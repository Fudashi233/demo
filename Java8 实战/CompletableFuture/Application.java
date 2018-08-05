package cn.edu.jxau;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/8/1
 * Time:下午3:28
 */
public class Application {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        Shop shop = new Shop("Seven-eleven");
        System.out.println("before invoke:" + System.currentTimeMillis());
        Future<Double> priceFuture = shop.getPriceAsync("book");
        System.out.println("after invoke:" + System.currentTimeMillis());
        // do something in here
        Double price = priceFuture.get(5, TimeUnit.SECONDS);
        System.out.println("thr price of book is:" + price);
        System.out.println("finish:" + System.currentTimeMillis());
    }
}
