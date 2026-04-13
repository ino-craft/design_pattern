package delegationProblem;
public class Main {

	public static void main(String[] args) {
		Worker[] workers = { new Dog("Baduki"), new Cat("Nabi"), new Robot(), new Dog("Bulldog") };
		workers[0].setEmployeeType(new Manager());
		workers[0].setSalary(500.0);
		workers[1].setEmployeeType(new Regular());
		workers[1].setSalary(200.0);
		workers[2].setEmployeeType(new Regular());
		workers[2].setSalary(300.0);
		workers[3].setEmployeeType(new Regular());
		workers[3].setSalary(500.0);

		for (int i = 0; i < workers.length; i++) {
			workers[i].doWork();
			if (workers[i] instanceof Sayable) {
				((Sayable) workers[i]).say();
			}
			System.out.println("worker [" + i + "] previous salary = " + workers[i].getSalary());
			workers[i].increaseSalary(10.0);
			System.out.println("worker [" + i + "] increased salary = " + workers[i].getSalary());
		}

		workers[3].setEmployeeType(new Manager());
		System.out.println("worker [3] previous salary = " + workers[3].getSalary());
		workers[3].increaseSalary(10.0);
		System.out.println("worker [3] increased salary = " + workers[3].getSalary());
	}
}

/*
public class Main {

	public static void main(String[] args) {		// 
		Worker worker[] = { new Dog("Baduki"), new Cat("Nabi"), new Robot(), new Dog("Bulldog"), new Insect() };
		worker[0].setEmployeeType(new Manager());
		worker[0].setSalary(500);		
		worker[1].setEmployeeType(new Regular());
		worker[1].setSalary(200);
		worker[2].setEmployeeType(new Regular());
		worker[2].setSalary(300);
		worker[3].setEmployeeType(new Regular());
		worker[3].setSalary(500);
		worker[4].setEmployeeType(new Regular());
		worker[4].setSalary(500);
		
		for (int i=0; i < worker.length; i++) {		
			worker[i].doWork();
			//((Sayable)worker[i]).say(); // will trigger exception for Insect
		}
		
		for (int i=0; i < worker.length; i++) {
			System.out.println("worker [" + i + "] : " + worker[i] + "'s previous salary = " + worker[i].getSalary());					
			worker[i].increaseSalary(10);
			System.out.println("worker [" + i + "] : " + worker[i] + "'s  increased salary = " + worker[i].getSalary());			
		}
		System.out.println("............. 1 year later ...............");
		// Now worker 3 got promoted!!
		double prevSalary = worker[3].getSalary();
		worker[3].setEmployeeType(new Manager(prevSalary));
		
		for (int i=0; i < worker.length; i++) {
			System.out.println("worker [" + i + "] : " + worker[i] + "'s  previous salary = " + worker[i].getSalary());					
			worker[i].increaseSalary(10);
			System.out.println("worker [" + i + "] : " + worker[i] + "'s  increased salary = " + worker[i].getSalary());			
		}
		
		
	}

}
*/
