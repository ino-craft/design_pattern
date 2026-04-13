package grader.behavior.iterator;

import headfirst.iterator.dinermerger.DinerMenu;
import headfirst.iterator.dinermerger.Iterator;
import headfirst.iterator.dinermerger.PancakeHouseMenu;
import headfirst.iterator.dinermerger.Waitress;

public class IteratorBehaviorTest {
    public static void main(String[] args) {
        PancakeHouseMenu breakfast = new PancakeHouseMenu();
        DinerMenu lunch = new DinerMenu();
        Waitress waitress = new Waitress(breakfast, lunch);

        if (!waitress.isItemVegetarian("Vegetarian BLT")) {
            throw new AssertionError("Vegetarian BLT should be vegetarian");
        }
        if (waitress.isItemVegetarian("BLT")) {
            throw new AssertionError("BLT should not be vegetarian");
        }

        Iterator iterator = breakfast.createIterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        if (count != 4) {
            throw new AssertionError("PancakeHouseMenu should expose 4 items, got " + count);
        }

        System.out.println("PASS iterator traversal");
    }
}
