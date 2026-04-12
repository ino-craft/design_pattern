package headfirst.strategy;

public class MiniDuckSimulator {
 
	public static void main(String[] args) {
 
		MallardDuck	mallard = new MallardDuck();
		RubberDuck	rubberDuckie = new RubberDuck();
		DecoyDuck	decoy = new DecoyDuck();
 
		ModelDuck	model = new ModelDuck();

		mallard.performQuack();
		rubberDuckie.performQuack();
		decoy.performQuack();
   
		model.performFly();	
		model.setFlyBehavior(new FlyRocketPowered());
		model.performFly();
	}

	/* 
	public static void main(String[] args) {
 
		MallardDuck	mallard = new MallardDuck();
		RubberDuck	rubberDuckie = new RubberDuck();
		DecoyDuck	decoy = new DecoyDuck(); 
		ModelDuck	model = new ModelDuck();
		Duck ducks[] = {mallard, rubberDuckie, decoy, model};
		
		model.setQuackBehavior(new DoubleQuack());
		model.performQuack();
		
		System.out.println("Before Setting EggBehavior ........ ");
		for (int i=0; i < ducks.length; i++) {
			ducks[i].display();
			ducks[i].performEgg();
		}
		
		System.out.println("After Letting MallardDuck Spawn ........ ");
		mallard.setEggBehavior(new SpawnEgg());		
		for (int i=0; i < ducks.length; i++) {
			ducks[i].display();
			ducks[i].performEgg();
		}
		
		System.out.println("After Copying MallardDuck ........ ");
		model.CopyBehavior(mallard);
		mallard.setEggBehavior(new SpawnEgg());		
		for (int i=0; i < ducks.length; i++) {
			ducks[i].display();
			ducks[i].performEgg();
		}
	}
 */
}

