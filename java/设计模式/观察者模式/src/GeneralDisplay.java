package cn.edu.jxau.test;

public class GeneralDisplay implements Observer, Display {

    private float temperature;
    private float humidity;
    private float pressure;
    private Subject weatherData;

    public GeneralDisplay(Subject weatherData) {

        weatherData.registerObserver(this);
        this.weatherData = weatherData;
    }

    @Override
    public void display() {
        System.out.printf("temperature:%.2f,humidity:%.2f,pressure:%.2f\n", temperature, humidity, pressure);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {

        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

}
