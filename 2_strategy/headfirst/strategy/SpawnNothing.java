package headfirst.strategy;

public class SpawnNothing implements EggBehavior {
    public void spawn() {
        System.out.println("I spawn nothing");
    }
}
