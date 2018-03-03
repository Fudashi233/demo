package cn.edu.jxau.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.RandomAccess;

public class WeatherData implements Subject {

    private ArrayList<Observer> observerList;
    private float temperature;
    private float humidity;
    private float pressure;
    
    public WeatherData() {
        observerList = new ArrayList<Observer>();
    }
    
    @Override
    public void registerObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        
        if(observerList instanceof RandomAccess) {
            for(Observer observer : observerList) {
                observer.update(temperature, humidity, pressure);
            }
        } else {
            Iterator<Observer> iterator = observerList.iterator();
            while(iterator.hasNext()) {
                iterator.next().update(temperature, humidity, pressure);
            }
        }
    }
    
    public void measure(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        notifyObservers();
    }
}
