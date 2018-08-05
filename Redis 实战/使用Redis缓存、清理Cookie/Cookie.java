package cn.edu.jxau.demo.redis;

import lombok.Cleanup;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * Desc:
 * 【login:】：hash，存储token和用户名的映射
 * 【recent:】：zset，记录最近登录用户
 * 【viewed:${token}】：zset，记录最近浏览过的商品
 * <p>
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/3/11
 * Time:下午8:48
 */
public class Cookie {

    private JedisPool jedisPool;

    public Cookie() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(30);
        config.setMaxTotal(30);
        this.jedisPool = new JedisPool(config, "127.0.0.1", 6379);
        CleanSession cleanSession =  new CleanSession(jedisPool);
        new Thread(cleanSession).start();
    }

    public static void main(String[] args) {

        Cookie cookie = new Cookie();
        cookie.login("20180101", "Fudashi233");
        cookie.login("20180102", "Clive");
        cookie.login("20180103", "fulei04");
        cookie.login("20180104", "fulei");
        cookie.login("20180105", "liuyuan");
        cookie.login("20180106", "hello");
        cookie.login("20180107", "Tom");
        cookie.login("20180108", "Lisa");
        cookie.login("20180109", "Beau");
        cookie.login("20180110", "Lala");
        cookie.login("20180111", "Haha");
        cookie.login("20180112", "fulei04");



        //cookie.update("20180101", "a-1");
        //cookie.update("20180101", "a-2");
        //cookie.update("20180101", "a-3");
        //cookie.update("20180101", "a-4");
        //cookie.update("20180101", "a-5");
        //cookie.update("20180101", "a-6");
        //cookie.update("20180101", "a-7");
        //cookie.update("20180101", "a-8");
    }

    /**
     * 登录
     *
     * @param token
     * @param username
     */
    public void login(String token, String username) {

        @Cleanup Jedis jedis = getJedis();
        jedis.hset("login:", token, username);
        jedis.zadd("recent:", System.currentTimeMillis(), token);
    }

    /**
     * 更新购物信息：最近浏览过的商品
     *
     * @param token
     * @param itemNum
     */
    public void update(String token, String itemNum) {

        @Cleanup Jedis jedis = getJedis();
        jedis.zadd("recent:", System.currentTimeMillis(), token);
        if (itemNum == null || itemNum.isEmpty()) {
            return;
        }
        jedis.zadd("viewed:" + token, System.currentTimeMillis(), itemNum);
        jedis.zremrangeByRank("viewed:" + token, 0, -6); // 仅保留最近的五个商品
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }
}

class CleanSession implements Runnable {

    private static final int LIMIT = 10;
    private volatile boolean quit;
    private JedisPool jedisPool;

    public CleanSession(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void quit() {
        this.quit = true;
    }

    @Override
    public void run() {

        while (!quit) {

            @Cleanup Jedis jedis = jedisPool.getResource();
            long size = jedis.zcard("recent:");
            if (size <= LIMIT) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            int len = (int) Math.min(size - LIMIT, 5);
            Set<String> tokenSet = jedis.zrange("recent:", 0, len - 1); //要删除的元素集
            // 删除【viewed:${token}】 //
            for (String token : tokenSet) {
                jedis.del("viewed:" + token);
                jedis.hdel("login:", token);
                jedis.zrem("recent:", token);
            }
        }
    }
}
