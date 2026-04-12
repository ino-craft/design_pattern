package delegationProblem;
public class Dog extends Canine implements Sayable {
	public Dog (String name) {
		this.name = name;
	}
	@Override
	public void say() {		
		bark();
	}
	private void bark() {
		System.out.println("I am " + this.name);
		System.out.println("Bark");
		System.out.println("------------------");
	}
	public void doWork() {
		System.out.println("Working like a dog...");
	}
}
