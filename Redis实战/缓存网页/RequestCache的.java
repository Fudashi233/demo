package cn.edu.jxau.demo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * 回调接口
 */
interface Callback<T> {

    T call();
}

/**
 * Desc:商品页面缓存
 * 【cache:${hash}】：string，缓存网页
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/3/22
 * Time:下午7:43
 */
public class RequestCache {


    private JedisPool jedisPool;

    public RequestCache() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(30);
        jedisPoolConfig.setMaxIdle(30);
        this.jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
    }

    public static void main(String[] args) throws MalformedURLException {


        URL url = new URL("http://www.baidu.com");
        RequestCache requestCache = new RequestCache();
        String content = requestCache.getResponse(url, new Callback<String>() {
            @Override
            public String call() {
                return "BBB";
            }
        });
        System.out.println(content);
    }

    public String getResponse(URL url, Callback<String> callback) {
        return getResponse(url, callback, false);
    }

    public String getResponse(URL url, Callback<String> callback, boolean force) {

        if (force || !canCache(url)) {
            return callback == null ? null : callback.call();
        }
        Jedis jedis = jedisPool.getResource();
        String content = jedis.get("cache:" + url.hashCode());
        if (content == null && callback != null) {
            content = callback.call();
            jedis.setex("cache:" + url.hashCode(), 10000, content);
        }
        return content;

    }

    private boolean canCache(URL url) {

        Map<String, List<String>> paramMap = getParamMap(url);
        if (paramMap.containsKey("_")) { //如果是静态网页
            return false;
        }
        return true;
    }

    private Map<String, List<String>> getParamMap(URL url) {

        String query = url.getQuery();
        if (query == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String, List<String>> paramMap = new HashMap();
        String[] kvArr = query.split("&"); //键值对数组
        for (String kv : kvArr) {
            String[] pair = kv.split("=");
            if (pair.length < 2) {
                throw new RuntimeException("url异常：" + url);
            }
            String key = pair[0];
            String value = pair[1];
            if (paramMap.containsKey(key)) {
                paramMap.get(key).add(value);
            } else {
                List<String> list = new ArrayList<>();
                list.add(value);
                paramMap.put(key, list);
            }
        }
        return paramMap;
    }
}
