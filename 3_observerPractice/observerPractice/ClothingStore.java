package observerPractice;

public class ClothingStore {
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
}
