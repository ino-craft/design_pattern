package Visitor.interfaceVisitor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InterfaceVisitorBehaviorTest {
    public static void main(String[] args) {
        CountingVisitor visitor = new CountingVisitor();
        new Car().accept(visitor);

        if (
            visitor.wheels != 4
                || visitor.engines != 1
                || visitor.bodies != 1
                || visitor.trunks != 1
                || visitor.cars != 1
        ) {
            throw new AssertionError(
                "Expected 4 wheels, 1 engine, 1 body, 1 trunk, 1 car but got "
                    + visitor.wheels + ", "
                    + visitor.engines + ", "
                    + visitor.bodies + ", "
                    + visitor.trunks + ", "
                    + visitor.cars
            );
        }

        String destroyOutput = capture(() -> new Car().accept(new CarElementDestroyVisitor()));
        assertContains(destroyOutput, "Destroying");
        assertContains(destroyOutput.toLowerCase(), "wheel");
        assertContains(destroyOutput.toLowerCase(), "engine");
        assertContains(destroyOutput.toLowerCase(), "body");
        assertContains(destroyOutput.toLowerCase(), "trunk");
        assertContains(destroyOutput.toLowerCase(), "car");

        System.out.println("PASS interface visitor extensions");
    }

    private static class CountingVisitor implements ICarElementVisitor {
        private int wheels;
        private int engines;
        private int bodies;
        private int trunks;
        private int cars;

        public void visit(Wheel wheel) {
            wheels++;
        }

        public void visit(Engine engine) {
            engines++;
        }

        public void visit(Body body) {
            bodies++;
        }

        public void visit(Trunk trunk) {
            trunks++;
        }

        public void visit(Car car) {
            cars++;
        }
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
