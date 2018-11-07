package cn.edu.jxau.masterworker;

import cn.edu.jxau.util.CodeUtils;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/7
 * Time:上午8:21
 */
public class Main {

    public static void main(String[] args) {

        Master<Integer, Long> master = new Master<>(new PlusWorker(), 3);
        for (int i = 0; i < 4; i++) {
            master.submit(i);
        }
        master.execute();
        while (!master.isComplete()) {
            CodeUtils.sleep(100);
        }
        System.out.println(master.getMergeResult());
    }
}
