package headfirst.strategy;

public class ModelDuck extends Duck {
	public ModelDuck() {
		flyBehavior = new FlyNoWay();
		quackBehavior = new Quack();
	}

	public void display() {
		System.out.println("I'm a model duck");
	}

	public void CopyBehavior(Duck source) {
		flyBehavior = source.flyBehavior;
		quackBehavior = source.quackBehavior;
		eggBehavior = source.eggBehavior;
	}
}
