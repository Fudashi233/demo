package cn.edu.jxau.test;

public interface Observer {
    
    /**
     * 
     * @param temp 温度
     * @param humidity 湿度
     * @param pressure 气压
     */
    void update(float temperature, float humidity, float pressure);
}
