package observerPractice;

import java.util.Observable;
import java.util.Observer;

public class ObserverBehaviorTest {
    public static void main(String[] args) {
        WeatherDataSubject subject = new WeatherDataSubject();
        if (!(subject instanceof Observable)) {
            throw new AssertionError("WeatherDataSubject must extend java.util.Observable");
        }

        Observer iceCreamStore = new IceCreamStore();
        RecordingObserver recorder = new RecordingObserver();
        subject.addObserver(iceCreamStore);
        subject.addObserver(recorder);

        subject.setMeasurements(30.0f, 0.0f);
        subject.notifyObservers();
        assertEquals(1, recorder.updates, "first weather notification");

        subject.setMeasurements(10.0f, 10.0f);
        subject.notifyObservers();
        assertEquals(2, recorder.updates, "second weather notification");

        System.out.println("PASS observer java util notifications");
    }

    private static class RecordingObserver implements Observer {
        private int updates;

        public void update(Observable observable, Object argument) {
            updates++;
        }
    }

    private static void assertEquals(int expected, int actual, String label) {
        if (expected != actual) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }
}
