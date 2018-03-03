package cn.edu.jxau.test;

import java.util.Observable;
import java.util.Observer;

public class ForecastDisplay implements Display,Observer {

    private Observable observable;
    private float temperature;
    private float humidity;
    private float pressure;
    
    public ForecastDisplay(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }
    
    @Override
    public void display() {
        System.out.printf("temperature=%.2f,humidity=%.2f,pressure=%.2f\n", temperature, humidity, pressure);
    }

    @Override
    public void update(Observable o, Object arg) {
        
        if (o instanceof WeatherData) {
            WeatherData temp = (WeatherData)o;
            temperature = temp.getTemperature()+1;
            humidity = temp.getHumidity()+1;
            pressure = temp.getPressure()+1;
        } else {
           throw new IllegalArgumentException("参数o必须是WeatherData的子类"); 
        }
    }

}
