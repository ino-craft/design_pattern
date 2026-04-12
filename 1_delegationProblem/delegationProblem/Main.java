package delegationProblem;
public class Main {

	public static void main(String[] args) {
		Worker worker[] = { new Dog("Baduki"), new Cat("Nabi"), new Robot(), new Dog("Bulldog") };
		
		int i;		
		for ( i = 0 ; i < worker.length-1; i++) {			
			worker[i].doWork();
			((Sayable)worker[i]).say();
		}
		worker[i].doWork();		
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