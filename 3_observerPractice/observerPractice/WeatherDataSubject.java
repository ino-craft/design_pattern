package observerPractice;

import java.util.Observable;

public class WeatherDataSubject extends Observable {
    private float temperature = 25.0f;
    private float rainfall = 0.0f;

    public static class WeatherData {
        public final float temperature;
        public final float rainfall;

        public WeatherData(float temperature, float rainfall) {
            this.temperature = temperature;
            this.rainfall = rainfall;
        }
    }

    public WeatherDataSubject() {
    }

    public WeatherDataSubject(UmbrellaStore us, ClothingStore cs) {
        addObserver(us);
        addObserver(cs);
    }

    public void setMeasurements(float temperature, float rainfall) {
        this.temperature = temperature;
        this.rainfall = rainfall;
        setChanged();
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
        setChanged();
    }

    public void setRainfall(float rainfall) {
        this.rainfall = rainfall;
        setChanged();
    }

    public float getTemperature() {
        return temperature;
    }

    public float getRainfall() {
        return rainfall;
    }

    public void notifyDataSetChanged() {
        notifyObservers();
    }

    public void notifyObservers() {
        setChanged();
        super.notifyObservers(new WeatherData(temperature, rainfall));
    }

    public void currentState() {
        System.out.printf(
            "----- Current state ----- \n Temperature : %.1f'c \n Rainfall : %.1fmm \n-------------------------\n",
            temperature,
            rainfall
        );
    }
}
