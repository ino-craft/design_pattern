package grader.behavior.decorator;

import headfirst.decorator.starbuzz.Beverage;
import headfirst.decorator.starbuzz.DarkRoast;
import headfirst.decorator.starbuzz.Mocha;
import headfirst.decorator.starbuzz.Whip;

public class StarbuzzDecoratorBehaviorTest {
    public static void main(String[] args) {
        Beverage beverage = new DarkRoast();
        beverage = new Mocha(beverage);
        beverage = new Mocha(beverage);
        beverage = new Whip(beverage);

        assertContains(beverage.getDescription(), "Dark Roast Coffee");
        assertContains(beverage.getDescription(), "Mocha, Mocha, Whip");
        assertNear(1.49, beverage.cost());

        System.out.println("PASS starbuzz decorator stack");
    }

    private static void assertContains(String actual, String expected) {
        if (!actual.contains(expected)) {
            throw new AssertionError("Expected description to contain " + expected + ", got " + actual);
        }
    }

    private static void assertNear(double expected, double actual) {
        if (Math.abs(expected - actual) > 0.0001) {
            throw new AssertionError("Expected cost " + expected + ", got " + actual);
        }
    }
}
