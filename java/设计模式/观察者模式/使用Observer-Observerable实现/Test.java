package cn.edu.jxau.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Objects;

public class Test {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        WeatherData weatherData = new WeatherData();
        GeneralDisplay display1 = new GeneralDisplay(weatherData);
        ForecastDisplay display2 = new ForecastDisplay(weatherData);
        weatherData.measure(12.2F,8F,5.7F);
        display1.display();
        display2.display();
        weatherData.measure(12.3F,9F,5.7F);
        display1.display();
        display2.display();
    }
}
