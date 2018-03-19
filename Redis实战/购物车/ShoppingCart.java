package cn.edu.jxau.demo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Desc:购物车
 * 【cart:${token}】：hash，购物车
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/3/19
 * Time:下午10:31
 */
public class ShoppingCart {

    private JedisPool jedisPool;

    public ShoppingCart() {
        

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(30);
        config.setMaxTotal(30);
        this.jedisPool = new JedisPool(config, "127.0.0.1", 6379);
        CleanSession cleanSession = new CleanSession(jedisPool);
        new Thread(cleanSession).start();
    }

    public static void main(String[] args) {

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.putItem("fudashi", "1", 5);
        System.out.println(shoppingCart.getItem("fudashi", "2"));
        System.out.println(shoppingCart.getItem("fudashi", "1"));
    }

    /**
     * 增加商品
     */
    public void putItem(String token, String item, int count) {

        if (count < 0) {
            throw new IllegalArgumentException("参数count不该小于0");
        }
        if (count == 0) {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        jedis.hset("cart:" + token, item, String.valueOf(count));
    }

    /**
     * 获取某件商品的数量
     *
     * @param token
     * @param item
     * @return
     */
    public int getItem(String token, String item) {

        Jedis jedis = jedisPool.getResource();
        String count = jedis.hget("cart:" + token, item);
        return count == null ? 0 : Integer.parseInt(count);
    }
}
