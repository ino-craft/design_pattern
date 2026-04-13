package headfirst.strategy;

public abstract class Duck {
	protected FlyBehavior flyBehavior;
	protected QuackBehavior quackBehavior;
	protected EggBehavior eggBehavior;
 
	public Duck() {
		eggBehavior = new SpawnNothing();
	}
 
	public void setFlyBehavior (FlyBehavior fb) {
		flyBehavior = fb;
	}
 
	public void setQuackBehavior(QuackBehavior qb) {
		quackBehavior = qb;
	}

	public void setEggBehavior(EggBehavior eb) {
		eggBehavior = eb;
	}
 
	abstract void display();
 
	public void performFly() {
		flyBehavior.fly();
	}
 
	public void performQuack() {
		quackBehavior.quack();
	}

	public void performEgg() {
		eggBehavior.spawn();
	}
 
	public void swim() {
		System.out.println("All ducks float, even decoys!");
	}
}
