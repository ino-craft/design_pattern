package observerPractice;

import java.util.Observable;
import java.util.Observer;

public class IceCreamStore implements Observer {
    public void update(Observable observable, Object argument) {
        float temperature = 0.0f;
        if (argument instanceof WeatherDataSubject.WeatherData) {
            temperature = ((WeatherDataSubject.WeatherData) argument).temperature;
        } else if (observable instanceof WeatherDataSubject) {
            temperature = ((WeatherDataSubject) observable).getTemperature();
        }

        if (temperature >= 20.0f) {
            System.out.println("Selling Ice Cream !");
        } else {
            System.out.println("Ice Cream Not Sale !");
        }
    }
}
