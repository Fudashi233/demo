package cn.edu.jxau.demo.redis;

import lombok.Cleanup;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * Desc:数据库缓存
 * 【inventory:${rowId}】：stirng，数据库缓存
 * 【schedule:】：zset，调度有序集合
 * 【delay:】：zset，延时有序集合
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/3/22
 * Time:下午10:29
 */
public class RowCache {

    public static void main(String[] args) {

        RowCache rowCache = new RowCache();
        rowCache.register("1",5000);
    }

    private JedisPool jedisPool;

    public RowCache() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(30);
        jedisPoolConfig.setMaxIdle(30);
        this.jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
        new Thread(new FetchTask(jedisPool)).start();
    }

    public void register(String rowId, long delay) {

        @Cleanup Jedis jedis = jedisPool.getResource();
        jedis.zadd("schedule:", 0, rowId);
        jedis.zadd("delay:", delay, rowId);
    }
}

/**
 * 从数据库中取出数据
 */
class FetchTask implements Runnable {

    private JedisPool jedisPool;
    private boolean quit;

    public FetchTask(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void quit() {
        quit = true;
    }

    @Override
    public void run() {

        while (!quit) {
            @Cleanup Jedis jedis = jedisPool.getResource();
            Set<Tuple> set = jedis.zrangeWithScores("schedule:", 0, 0);
            Tuple tuple = set.isEmpty() ? null : set.iterator().next();
            long now = System.currentTimeMillis();
            if (tuple == null || now < tuple.getScore()) { //调度时间未到或则调度集为空
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            String rowId = tuple.getElement();
            double delay = jedis.zscore("delay:", rowId);
            if (delay <= 0) { //删除缓存
                jedis.zrem("schedule:", rowId);
                jedis.zrem("delay:", rowId);
                jedis.del("inventory:" + rowId);
            } else { //更新缓存
                jedis.zadd("schedule:", delay + now, rowId);
                jedis.set("inventory:" + rowId, getRow(rowId));
            }
        }
    }

    private String getRow(String rowId) {

        System.out.println("--- getRow() ---");
        return rowId;
    }
}