package cn.edu.jxau.demo.redis;

import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;

import java.util.*;

/**
 * Desc: 使用 Redis 实现对热门文章的排序。
 * article: 充当计数器，用于计算 articleId。
 * score: 和 time: 分别是投票获得的分数以及发布时间对应的分数
 * voted:[articleId] 指对应文章投票过的人
 * article:[articleId] 指对应文章的信息
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/2/26
 * Time:下午4:28
 */
public class ArticleSort {

    private static final int ONE_WEEK_IN_SECONDS = 60 * 60 * 24 * 7;
    private static final int VOTE_SCORE = 432; //削弱时间对排名的影响
    private static final int PAGE_SIZE = 10;

    /**
     * 发布文章
     *
     * @param conn
     * @param author
     * @param title
     * @param link
     */
    public void postArticle(Jedis conn, String author, String title, String link) {

        Long articleId = conn.incr("article:");
        String voted = "voted:" + articleId;
        conn.sadd(voted, author);
        conn.expire(voted, ONE_WEEK_IN_SECONDS); //一周后不可再投票
        long now = System.currentTimeMillis() / 1000;
        Map<String, String> articleInfo = getArticleInfo(author, title, link, now);
        String article = "article:" + articleId;
        conn.hmset(article, articleInfo);
        conn.zadd("score:", 0, article);
        conn.zadd("time:", now, article);
    }

    private Map<String, String> getArticleInfo(String author, String title, String link, long postDatetime) {

        Map<String, String> articleInfo = new HashMap<>();
        articleInfo.put("title", title);
        articleInfo.put("link", link);
        articleInfo.put("author", author);
        articleInfo.put("postDatetime", String.valueOf(postDatetime));
        articleInfo.put("voteCount", String.valueOf(0));
        return articleInfo;
    }

    /**
     * 投票
     *
     * @param conn
     * @param voter
     * @param articleId
     * @return
     */
    public boolean vote(Jedis conn, String voter, long articleId) {

        // 判断是否还可以投票 //
        long now = System.currentTimeMillis() / 1000;
        double postDatetime = conn.zscore("time:", "article:" + articleId);
        if (postDatetime + ONE_WEEK_IN_SECONDS < now) { //超过一星期，不可再投票
            return false;
        }
        // 投票 //
        if (conn.sadd("voted:" + articleId, voter).equals(1L)) {
            conn.zincrby("score:", VOTE_SCORE, "article:" + articleId);
            conn.hincrBy("article:" + articleId, "voteCount", 1);
            return true;
        }
        return false;
    }

    /**
     * 获取指定页的文章，按发布时间排序
     *
     * @return
     */
    public List<Map<String, String>> getArticleInfoOrderByTime(Jedis conn, int page) {

        if (page <= 0) {
            throw new IllegalArgumentException("page = " + page + ", page 需是大于1的整数");
        }
        return getArticleInfoList(conn, page, "time:");
    }

    /**
     * 获取指定页的文章，按投票数排序
     *
     * @return
     */
    public List<Map<String, String>> getArticleInfoOrderByVote(Jedis conn, int page) {

        if (page <= 0) {
            throw new IllegalArgumentException("page = " + page + ", page 需是大于1的整数");
        }
        return getArticleInfoList(conn, page, "score:");
    }

    public List<Map<String, String>> getArticleInfoByGroupOrderByVote(Jedis conn, int groupId, int page) {

        if (page <= 0) {
            throw new IllegalArgumentException("page = " + page + ", page 需是大于1的整数");
        }
        String zset = "source:" + groupId;
        conn.zinterstore(zset, "group:" + groupId, "score:");
        return getArticleInfoList(conn, page, zset);
    }

    private List<Map<String, String>> getArticleInfoList(Jedis conn, int page, String zset) {

        int start = (page - 1) * PAGE_SIZE;
        int end = start + PAGE_SIZE - 1;
        Set<String> set = conn.zrevrange(zset, start, end);
        if (CollectionUtils.isEmpty(set)) {
            return Collections.EMPTY_LIST;
        }
        List<Map<String, String>> articleInfoList = new ArrayList<>();
        for (String article : set) {
            Map<String, String> articleInfo = conn.hgetAll(article);
            articleInfoList.add(articleInfo);
        }
        return articleInfoList;
    }

    public boolean addToGroup(Jedis conn, int articleId, int groupId) {

        if (conn.sadd("group:" + groupId, "article:" + articleId).equals(1L)) {
            return true;
        }
        return false;
    }

    public boolean removeFromGroup(Jedis conn, int articleId, int groupId) {

        if (conn.srem("group:" + groupId, "article:" + articleId).equals(1L)) {
            return true;
        }
        return false;
    }
}
