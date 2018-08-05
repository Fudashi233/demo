package cn.edu.jxau.demo.redis;

import lombok.Cleanup;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Desc:
 * 【inventory:${userId}】：set，用户仓库
 * 【market:】：zset，市场
 * 【users:${userId}】：hash，存储用户的基本信息
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/3/25
 * Time:下午10:11
 */
public class Market {


    private static final int TRY_LIMIT = 5;
    private JedisPool jedisPool;
    public Market() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(30);
        jedisPoolConfig.setMaxIdle(30);
        this.jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
    }

    public static void main(String[] args) {


        Market market = new Market();
        //System.out.println(market.sale("1", "item1", 19));
        //System.out.println(market.purchase("2", "1", "item1"));
    }

    /**
     * @param sellerId 销售者id
     * @param itemId   商品id
     * @param price    价格
     */
    public boolean sale(String sellerId, String itemId, long price) {

        String inventory = "inventory:" + sellerId;
        String item = itemId + "." + sellerId;
        @Cleanup Jedis jedis = jedisPool.getResource();
        for (int i = 0; i < TRY_LIMIT; i++) {
            jedis.watch(inventory);
            if (!jedis.sismember(inventory, itemId)) {
                jedis.unwatch();
                return false;
            }
            Transaction tx = jedis.multi();
            tx.zadd("market:", price, item);
            tx.srem(inventory, itemId);
            List list = tx.exec();
            if (list != null) {
                return true;
            }
        }
        return false;
    }

    public boolean purchase(String purchaserId, String sellerId, String itemId) {

        String seller = "users:" + sellerId;
        String purchaser = "users:" + purchaserId;
        String item = itemId + "." + sellerId;
        String inventory = "inventory:" + purchaserId;
        @Cleanup Jedis jedis = jedisPool.getResource();
        for (int i = 0; i < TRY_LIMIT; i++) {
            jedis.watch(seller, purchaser);
            double property = Double.parseDouble(jedis.hget(purchaser, "property"));
            Double price = jedis.zscore("market:", item);
            if (price == null) {
                jedis.unwatch();
                throw new NullPointerException("要购买的商品不存在");
            }
            if (price > property) { //钱不够
                jedis.unwatch();
                return false;
            }
            Transaction tx = jedis.multi();
            tx.hincrBy(seller, "property", (long) (price.doubleValue()));
            tx.hincrBy(purchaser, "property", -(long) (price.doubleValue()));
            tx.zrem("market:", item);
            tx.sadd(inventory, itemId);
            tx.exec();
        }
        return false;
    }
}
