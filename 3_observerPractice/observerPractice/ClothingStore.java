package observerPractice;

import java.util.Observable;
import java.util.Observer;

public class ClothingStore implements Observer {
	private static final int SUMMER_CLOTHS = 1;
	private static final int WINTER_CLOTHS = 2;
	
	private int state;
	
	public ClothingStore() {
		state = SUMMER_CLOTHS;
	}
	
	public void weatherChanged(float temperature) {
		if(temperature >= 15.0f) // when temperature exceeds 15.0'c
			state = SUMMER_CLOTHS; // selling summer cloths
		else // when temperature < 15.0
			state = WINTER_CLOTHS;
		
		kindOfSellingCloths();
	}
	public void kindOfSellingCloths() {
		System.out.println("Selling " + ((state == SUMMER_CLOTHS) ? "Summer" : "Winter") + " cloths !");
	}

	public void update(Observable observable, Object argument) {
		if (argument instanceof WeatherDataSubject.WeatherData) {
			weatherChanged(((WeatherDataSubject.WeatherData) argument).temperature);
		} else if (observable instanceof WeatherDataSubject) {
			weatherChanged(((WeatherDataSubject) observable).getTemperature());
		}
	}
}
