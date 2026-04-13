package grader.behavior.observer;

import observerPractice.ClothingStore;
import observerPractice.UmbrellaStore;
import observerPractice.WeatherDataSubject;

public class ObserverBehaviorTest {
    public static void main(String[] args) {
        WeatherDataSubject subject = new WeatherDataSubject(
            new UmbrellaStore(),
            new ClothingStore()
        );

        subject.setMeasurements(10.0f, 10.0f);
        subject.notifyDataSetChanged();

        subject.setMeasurements(20.0f, 1.0f);
        subject.notifyDataSetChanged();

        System.out.println("PASS observer notifications");
    }
}
