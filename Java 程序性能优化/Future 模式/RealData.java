package cn.edu.jxau.future;

import cn.edu.jxau.util.CodeUtils;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Data:2018/11/5
 * Time:上午8:21
 */
public class RealData implements Data {

    private String result;

    public RealData(String result) {
        CodeUtils.sleep(3000); // 模拟一个性能低下的构造函数
        this.result = "echo" + result;
    }

    @Override
    public String getResult() {
        return result;
    }
}
