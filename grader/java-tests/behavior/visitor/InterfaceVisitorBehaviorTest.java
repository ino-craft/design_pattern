package Visitor.interfaceVisitor;

public class InterfaceVisitorBehaviorTest {
    public static void main(String[] args) {
        CountingVisitor visitor = new CountingVisitor();
        new Car().accept(visitor);

        if (visitor.wheels != 4 || visitor.engines != 1 || visitor.bodies != 1 || visitor.cars != 1) {
            throw new AssertionError(
                "Expected 4 wheels, 1 engine, 1 body, 1 car but got "
                    + visitor.wheels + ", "
                    + visitor.engines + ", "
                    + visitor.bodies + ", "
                    + visitor.cars
            );
        }

        System.out.println("PASS interface visitor traversal");
    }

    private static class CountingVisitor implements ICarElementVisitor {
        private int wheels;
        private int engines;
        private int bodies;
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

        public void visit(Car car) {
            cars++;
        }
    }
}
