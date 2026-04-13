package headfirst.strategy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StrategyBehaviorTest {
    public static void main(String[] args) {
        ModelDuck model = new ModelDuck();

        model.setQuackBehavior(new DoubleQuack());
        assertContains(capture(model::performQuack), "Quack, Quack");

        Duck mallard = new MallardDuck();
        mallard.setFlyBehavior(new FlyRocketPowered());
        mallard.setQuackBehavior(new DoubleQuack());
        mallard.setEggBehavior(new SpawnEgg());
        assertContains(capture(mallard::performEgg), "Spawned");

        model.setFlyBehavior(new FlyNoWay());
        model.setQuackBehavior(new MuteQuack());
        model.setEggBehavior(new SpawnNothing());
        model.CopyBehavior(mallard);

        String copiedOutput = capture(() -> {
            model.performFly();
            model.performQuack();
            model.performEgg();
        });
        assertContains(copiedOutput, "I'm flying with a rocket");
        assertContains(copiedOutput, "Quack, Quack");
        assertContains(copiedOutput, "Spawned");

        System.out.println("PASS strategy behavior extensions");
    }

    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return output.toString();
    }

    private static void assertContains(String actual, String expected) {
        if (!actual.contains(expected)) {
            throw new AssertionError("Expected output to contain " + expected + ", got:\n" + actual);
        }
    }
}
