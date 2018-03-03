package cn.edu.jxau.test;

import java.util.Observable;

public class WeatherData extends Observable {
    
    private float temperature;
    private float humidity;
    private float pressure;
    
    public WeatherData() {
        
    }
    
    public void measure(float temperature, float humidity, float pressure) {
        
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        super.setChanged();
        super.notifyObservers();
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }
}
