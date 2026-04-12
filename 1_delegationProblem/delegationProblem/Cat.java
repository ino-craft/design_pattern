package delegationProblem;
public class Cat extends Animal implements Sayable {
	public Cat (String name) {
		this.name = name;
	}
	@Override
	public void say() {
		meow();
	}
	private void meow() {
		System.out.println("I am " + this.name);
		System.out.println("Meow");
		System.out.println("------------------");
	}
	public void doWork() {
		System.out.println("Working like a cat...");
	}
}
