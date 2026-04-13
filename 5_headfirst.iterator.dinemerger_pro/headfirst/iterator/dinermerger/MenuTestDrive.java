package headfirst.iterator.dinermerger;

import java.util.*;

public class MenuTestDrive {
	public static void main(String args[]) {
		// printMenu();
		iteratorPrintMenu();
	}

	public static void printMenu() {
		PancakeHouseMenu pancakeHouseMenu = new PancakeHouseMenu();
		DinerMenu dinerMenu = new DinerMenu();

		ArrayList breakfastItems = pancakeHouseMenu.getMenuItems();
 
		for (int i = 0; i < breakfastItems.size(); i++) {
			MenuItem menuItem = (MenuItem)breakfastItems.get(i);
			System.out.print(menuItem.getName());
			System.out.println("\t\t" + menuItem.getPrice());
			System.out.println("\t" + menuItem.getDescription());
		}

		MenuItem[] lunchItems = dinerMenu.getMenuItems();
 
		for (int i = 0; i < lunchItems.length; i++) {
			MenuItem menuItem = lunchItems[i];
			System.out.print(menuItem.getName());
			System.out.println("\t\t" + menuItem.getPrice());
			System.out.println("\t" + menuItem.getDescription());
		}
	}
	
	public static void iteratorPrintMenu() {
		PancakeHouseMenu pancakeHouseMenu = new PancakeHouseMenu();
        DinerMenu dinerMenu = new DinerMenu();
		CafeMenu cafeMenu = new CafeMenu();
 
		Waitress waitress = new Waitress(pancakeHouseMenu, dinerMenu, cafeMenu);
		waitress.printMenu();
	}

}

/*
public static void iteratorPrintMenu() {
	PancakeHouseMenu pancakeHouseMenu = new PancakeHouseMenu();
	DinerMenu dinerMenu = new DinerMenu();
	CafeMenu cafeMenu = new CafeMenu();

	Waitress waitress = new Waitress(pancakeHouseMenu, dinerMenu, cafeMenu);

	waitress.printMenu();
	waitress.printVegetarianMenu();

	System.out.println("\nCustomer asks, is the Hotdog vegetarian?");
	System.out.print("Waitress says: ");
	if (waitress.isItemVegetarian("Hotdog")) {
		System.out.println("Yes");
	} else {
		System.out.println("No");
	}
	System.out.println("\nCustomer asks, are the Waffles vegetarian?");
	System.out.print("Waitress says: ");
	if (waitress.isItemVegetarian("Waffles")) {
		System.out.println("Yes");
	} else {
		System.out.println("No");
	}
}
*/
