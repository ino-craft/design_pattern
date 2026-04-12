package delegationProblem;
public class Insect extends Worker {	
	public void say() {		
		printOut();
	}
	private void printOut() {
		System.out.println("I have no name. I am an Insect");
		System.out.println("buzz");
		System.out.println("------------------");
	}
	
	public void doWork() {
		System.out.println("I hate working since I am an insect ....");
	}
}
