package cn.edu.jxau.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Page {

    private AtomicInteger count;
    private Map<String, Date> map;
    private static final int PERIOD = 1; // IP有效期，在这个时间段内的访问行为不计入访问量，单位是秒
    private static final long PERIOD_MILLISECOND = PERIOD * 1000; // PERIOD的毫秒值

    public Page() {
        count = new AtomicInteger();
        map = new ConcurrentHashMap<>();
        Thread cleaner = new Thread(new MapCleaner());
        cleaner.setDaemon(true);
        cleaner.start();
    }

    public int getCount() {
        return count.get();
    }

    public int getMapSize() {
        return map.size();
    }

    public int visit() {
        return getCount() + getMapSize();
    }

    private void increment(int i) {

        while (!count.compareAndSet(count.get(), count.get()+i)) {
            // 循环，直至设置成功
        }
    }

    public void access(Request request) {

        if (!map.containsKey(request.getIP())) {
            map.put(request.getIP(), request.getDate());
        }
    }

    @Override
    public String toString() {
        return String.format("访问量：%d，count：%d，map:%d", visit(), getCount(), getMapSize());
    }

    private class MapCleaner implements Runnable {

        @Override
        public void run() {

            try {
                while (true) {
                    Thread.sleep(1000);
                    increment(clean());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("清理线程中断", e);
            }
        }

        /**
         * 清除map中过期的键值对
         * @return
         */
        private int clean() {

            Set<Entry<String, Date>> entrySet = map.entrySet();
            List<String> cleanList = new ArrayList<>(); // 存储需要删除的键值对的建值

            // 查找过期的键值对 //
            long now = System.currentTimeMillis();
            for (Entry<String, Date> entry : entrySet) {
                String ip = entry.getKey();
                Date date = entry.getValue();
                System.out.printf("%d,%d,%d\n", date.getTime(), date.getTime() + PERIOD_MILLISECOND, now);
                if (date.getTime() + PERIOD_MILLISECOND <= now) { // 访问已过期,需清理
                    cleanList.add(ip);
                }
            }

            // 清除过期的键值对 //
            for (String str : cleanList) {
                map.remove(str);
            }
            return cleanList.size();
        }
    }
}
