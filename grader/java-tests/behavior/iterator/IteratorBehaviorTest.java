package headfirst.iterator.dinermerger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

public class IteratorBehaviorTest {
    public static void main(String[] args) {
        PancakeHouseMenu breakfast = new PancakeHouseMenu();
        DinerMenu lunch = new DinerMenu();
        CafeMenu cafe = new CafeMenu();
        Waitress waitress = new Waitress(breakfast, lunch, cafe);

        if (!waitress.isItemVegetarian("Vegetarian BLT")) {
            throw new AssertionError("Vegetarian BLT should be vegetarian");
        }
        if (waitress.isItemVegetarian("BLT")) {
            throw new AssertionError("BLT should not be vegetarian");
        }

        Iterator breakfastIterator = breakfast.createIterator();
        if (breakfastIterator.getClass().getName().contains("PancakeHouseMenuIterator")) {
            throw new AssertionError("PancakeHouseMenu should use the Java collection iterator");
        }

        Iterator lunchIterator = lunch.createIterator();
        if (!lunchIterator.getClass().getSimpleName().equals("AlternatingDinerMenuIterator")) {
            throw new AssertionError("DinerMenu should use AlternatingDinerMenuIterator");
        }

        int cafeCount = 0;
        boolean sawBurrito = false;
        Iterator cafeIterator = cafe.createIterator();
        while (cafeIterator.hasNext()) {
            MenuItem item = (MenuItem) cafeIterator.next();
            cafeCount++;
            sawBurrito = sawBurrito || item.getName().equals("Burrito");
        }
        if (cafeCount < 3 || !sawBurrito) {
            throw new AssertionError("CafeMenu should expose the Hashtable-backed cafe items");
        }

        String menuOutput = capture(waitress::printMenu);
        assertContains(menuOutput, "Veggie Burger and Air Fries");
        assertContains(menuOutput, "Burrito");

        System.out.println("PASS iterator cafe traversal");
    }

    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return output.toString();
    }

    private static void assertContains(String actual, String expected) {
        if (!actual.contains(expected)) {
            throw new AssertionError("Expected output to contain " + expected + ", got:\n" + actual);
        }
    }
}
