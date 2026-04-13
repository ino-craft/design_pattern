package grader.behavior.strategy;

import headfirst.strategy.Duck;
import headfirst.strategy.FlyRocketPowered;
import headfirst.strategy.ModelDuck;
import headfirst.strategy.MuteQuack;

public class StrategyBehaviorTest {
    public static void main(String[] args) {
        Duck duck = new ModelDuck();

        duck.performFly();
        duck.setFlyBehavior(new FlyRocketPowered());
        duck.performFly();
        duck.setQuackBehavior(new MuteQuack());
        duck.performQuack();

        System.out.println("PASS strategy runtime swap");
    }
}
