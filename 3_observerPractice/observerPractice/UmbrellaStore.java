package observerPractice;

import java.util.Observable;
import java.util.Observer;

public class UmbrellaStore implements Observer {
	
	private boolean sellingState;
	// default constructor
	public UmbrellaStore() {
		this.sellingState = false;
	}
	
	public void weatherChanged(float rainfall) {
		if (rainfall >= 5.0f)// when rainfall exceeds 5.0mm
			sellingState = true; // selling umbrella
		else // when rainfall < 5.0
			sellingState = false;
		
		isSelling();
	}

	public void isSelling() {
		System.out.println("Umbrella "+ ((sellingState) ? "":"Not ") + "Sale !");
	}

	public void update(Observable observable, Object argument) {
		if (argument instanceof WeatherDataSubject.WeatherData) {
			weatherChanged(((WeatherDataSubject.WeatherData) argument).rainfall);
		} else if (observable instanceof WeatherDataSubject) {
			weatherChanged(((WeatherDataSubject) observable).getRainfall());
		}
	}
}
