package cn.edu.jxau;

import cn.edu.jxau.util.CodeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/8/5
 * Time:下午4:18
 */
@Data
@AllArgsConstructor
public class Shop {

    private String name;

    public double getPrice(String productName) {

        CodeUtils.sleep(1000);
        return Math.random() * productName.charAt(0) + productName.charAt(1);
    }

    public Future<Double> getPriceAsync(String productName) {

        CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread(() -> {
            double price = getPrice(productName);
            future.complete(price);
        }).start();
        return future;
    }

    public Future<Double> getPriceWithException(String productName) {

        CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = getPrice(productName);
                throw new RuntimeException("product not availables");
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }).start();
        return future;
    }
}
