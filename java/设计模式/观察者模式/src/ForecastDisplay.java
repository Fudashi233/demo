package cn.edu.jxau.test;

public class ForecastDisplay implements Display,Observer {

    private float temperature;
    private float humidity;
    private float pressure;
    
    private Subject weatherData;

    public ForecastDisplay(Subject weatherData) {
        weatherData.registerObserver(this);
        this.weatherData = weatherData;
    }
    
    @Override
    public void update(float temperature, float humidity, float pressure) {
        
        this.temperature = (float) (temperature*0.2);
        this.humidity = (float) (humidity+temperature*0.1);
        this.pressure = (float) (pressure * 0.1);
    }

    @Override
    public void display() {
        System.out.printf("temperature:%.2f,humidity:%.2f,pressure:%.2f\n",temperature,humidity,pressure);
    }
}
