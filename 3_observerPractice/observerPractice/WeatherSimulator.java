package observerPractice;

public class WeatherSimulator {
	
	
	public static void main(String[] args) {
		WeatherDataSubject wdSubject;
		//observer objects
		UmbrellaStore umbrellaStore = new UmbrellaStore();
		ClothingStore clothingStore = new ClothingStore();
		
		//subject object
		wdSubject = new WeatherDataSubject(umbrellaStore, clothingStore);
		
		wdSubject.currentState();
		wdSubject.notifyDataSetChanged();
		
		// weather changed : temperature = 10.0'c, rainfall = 10.0mm
		System.out.println();
		wdSubject.setMeasurements(10.0f, 10.0f);
		wdSubject.currentState();
		wdSubject.notifyDataSetChanged();
		
		// weather changed : temperature = 16.0'c, rainfall = 1.0mm
		System.out.println();
		wdSubject.setMeasurements(16.0f, 1.0f);
		wdSubject.currentState();
		wdSubject.notifyDataSetChanged();
	}
}

/* 
public static void main(String[] args) {
	WeatherDataSubject wdSubject;
	//observer objects
	UmbrellaStore umbrellaStore = new UmbrellaStore();
	ClothingStore clothingStore = new ClothingStore();
	IceCreamStore iceCreamStore = new IceCreamStore();
	//subject object
	wdSubject = new WeatherDataSubject();
	
	// add observer
	wdSubject.addObserver(umbrellaStore);
	wdSubject.addObserver(clothingStore);
	wdSubject.addObserver(iceCreamStore);
	//display current subject's state
	wdSubject.currentState();
	// notify observers, 
	wdSubject.notifyObservers();
	// weather changed : temperature = 10.0'c, rainfall = 10.0mm
	System.out.println();
	wdSubject.setMeasurements(10.0f, 10.0f);
	wdSubject.currentState();
	wdSubject.notifyObservers();
	// weather changed : temperature = 16.0'c, rainfall = 1.0mm
	System.out.println();
	wdSubject.setMeasurements(16.0f, 1.0f);
	wdSubject.currentState();
	wdSubject.notifyObservers();
	// weather changed : temperature = 30.0'c, rainfall = 0.0mm
	System.out.println();
	wdSubject.setMeasurements(30.0f, 0.0f);
	wdSubject.currentState();
	wdSubject.notifyObservers();
}
 */