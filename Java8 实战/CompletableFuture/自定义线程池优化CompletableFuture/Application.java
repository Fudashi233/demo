package cn.edu.jxau;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/8/1
 * Time:下午3:28
 */
public class Application {


    private static List<Shop> shopList = Arrays.asList(new Shop("A"),
            new Shop("B"),
            new Shop("C"),
            new Shop("D"),
            new Shop("E"),
            new Shop("F"),
            new Shop("G"),
            new Shop("H"),
            new Shop("I"),
            new Shop("J"));

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        //System.out.println(Runtime.getRuntime().availableProcessors());
        //findPrice0("book");
        //findPrice1("book");
        //findPrice2("book");
        findPrice3("book");
    }

    /**
     * 普通的stream，顺序调用getPrice（）方法，造成计算资源的浪费
     *
     * @param productName
     * @return
     */
    public static List<String> findPrice0(String productName) {

        long start = System.currentTimeMillis();
        List<String> result = shopList.stream().map(shop -> String.format("shop name:%s,price of product:%f", shop.getName(), shop.getPrice(productName)))
                .collect(Collectors.toList());
        long end = System.currentTimeMillis();
        System.out.println("duration：" + (end - start));
        return result;
    }

    /**
     * 并行stream，并行的调用getPrice()方法，计算资源被充分利用，但不完全。
     * 因为并行流利用的线程数与机器的线城数通常保持一致，但对于IO密集型的应用，计算资源任有一部分被消耗
     *
     * @param productName
     * @return
     */
    public static List<String> findPrice1(String productName) {

        long start = System.currentTimeMillis();
        List<String> result = shopList.parallelStream().map(shop -> String.format("shop name:%s,price of product:%f", shop.getName(), shop.getPrice(productName)))
                .collect(Collectors.toList());
        long end = System.currentTimeMillis();
        System.out.println("duration：" + (end - start));
        return result;
    }

    /**
     * 利用ForkJoinPool.commonPool()返回的线程池并行调用getPrice()方法，
     * 本质与findPrice1()并行流的方式一样
     *
     * @param productName
     * @return
     */
    public static List<String> findPrice2(String productName) {

        long start = System.currentTimeMillis();
        List<CompletableFuture<String>> completableFutureList = shopList.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("shop name:%s,price of product:%f", shop.getName(), shop.getPrice(productName))))
                .collect(Collectors.toList());

        List<String> result = completableFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long end = System.currentTimeMillis();
        System.out.println("duration：" + (end - start));
        return result;

        /**
         * 由于【循环合并】，两个map被合并到一个迭代中，所以这样写实质就是顺序的效果
         */
        //long start = System.currentTimeMillis();
        //List<String> result = shopList.stream()
        //        .map(shop -> {
        //            System.out.println("map 1");
        //            return CompletableFuture.supplyAsync(
        //                    () -> String.format("shop name:%s,price of product:%f", shop.getName(), shop.getPrice(productName)));
        //        })
        //        .map(completableFuture -> {
        //            System.out.println("map 2");
        //            return completableFuture.join();
        //        })
        //        .collect(Collectors.toList());
        //
        //long end = System.currentTimeMillis();
        //System.out.println("duration：" + (end - start));
        //return result;
    }

    /**
     * 利用自定义的线程池执行任务，让计算资源最大化
     *
     * @param productName
     * @return
     */
    public static List<String> findPrice3(String productName) {

        ExecutorService executorService = Executors.newFixedThreadPool(shopList.size(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
        long start = System.currentTimeMillis();
        List<CompletableFuture<String>> completableFutureList = shopList.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("shop name:%s,price of product:%f", shop.getName(), shop.getPrice(productName)), executorService))
                .collect(Collectors.toList());

        List<String> result = completableFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long end = System.currentTimeMillis();
        System.out.println("duration：" + (end - start));
        return result;
    }
}
