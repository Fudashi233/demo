package cn.edu.jxau.test;

import java.util.Observable;
import java.util.Observer;

public class GeneralDisplay implements Observer,Display {
    
    private Observable observable;
    private float temperature;
    private float humidity;
    private float pressure;
    
    public GeneralDisplay(Observable observable) {
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
            temperature = temp.getTemperature();
            humidity = temp.getHumidity();
            pressure = temp.getPressure();
        } else {
           throw new IllegalArgumentException("参数o必须是WeatherData的子类"); 
        }
    }
}
