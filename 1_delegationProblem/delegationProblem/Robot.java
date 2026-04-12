package delegationProblem;
public class Robot extends Worker implements Sayable {

	@Override
	public void say() {		
		printOut();
	}
	private void printOut() {
		System.out.println("I have no name. I am a Robot");
		System.out.println("Beep");
		System.out.println("------------------");
	}
	
	public void doWork() {
		System.out.println("I love working since I am a robot ....");
	}
}
