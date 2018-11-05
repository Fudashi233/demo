package cn.edu.jxau.future;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/5
 * Time:上午8:31
 */
public class Client {

    public Data request(String queryStr) {

        FutureData futureData = new FutureData();
        asyncRequest(futureData, queryStr);
        return futureData;
    }

    private void asyncRequest(FutureData futureData, String queryStr) {

        new Thread(() -> {
            futureData.setRealData(new RealData(queryStr));
        }).start();
    }

}
