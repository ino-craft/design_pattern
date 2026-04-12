package observerPractice;

public class UmbrellaStore {
	
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
}
