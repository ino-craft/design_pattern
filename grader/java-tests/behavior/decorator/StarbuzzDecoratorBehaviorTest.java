package grader.behavior.decorator;

import headfirst.decorator.starbuzz.Beverage;
import headfirst.decorator.starbuzz.DarkRoast;
import headfirst.decorator.starbuzz.HouseBlend;
import headfirst.decorator.starbuzz.Mocha;
import headfirst.decorator.starbuzz.Soy;
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

        Beverage forward = new HouseBlend();
        forward = new Soy(forward);
        forward = new Mocha(forward);
        forward = new Mocha(forward);
        forward = new Whip(forward);
        forward = new Whip(forward);
        assertContains(forward.getDescription(), "House Blend Coffee, Soy, Mocha, Mocha, Whip, Whip");
        assertNear(1.64, forward.cost());

        Beverage reverse = new HouseBlend();
        reverse = new Whip(reverse);
        reverse = new Whip(reverse);
        reverse = new Mocha(reverse);
        reverse = new Mocha(reverse);
        reverse = new Soy(reverse);
        assertContains(reverse.getDescription(), "House Blend Coffee, Whip, Whip, Mocha, Mocha, Soy");
        assertNear(1.64, reverse.cost());

        System.out.println("PASS starbuzz decorator practice stack");
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
