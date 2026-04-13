package delegationProblem;

public class DelegationBehaviorTest {
    private static final double EPSILON = 0.0001;

    public static void main(String[] args) {
        if (!(new Regular() instanceof EmployeeType)) {
            throw new AssertionError("Regular must be usable as an EmployeeType");
        }
        if (!(new Manager() instanceof EmployeeType)) {
            throw new AssertionError("Manager must be usable as an EmployeeType");
        }

        Worker worker = new Dog("Buddy");

        worker.setSalary(100.0);
        worker.setEmployeeType(new Regular());
        worker.increaseSalary(10.0);
        assertNear(110.0, worker.getSalary(), "Regular salary increase");

        worker.setSalary(100.0);
        worker.setEmployeeType(new Manager());
        worker.increaseSalary(10.0);
        assertNear(121.0, worker.getSalary(), "Manager salary increase");

        worker.setSalary(200.0);
        worker.setEmployeeType(new Regular());
        worker.increaseSalary(10.0);
        assertNear(220.0, worker.getSalary(), "Regular salary before promotion");

        worker.setEmployeeType(new Manager());
        worker.increaseSalary(10.0);
        assertNear(253.0, worker.getSalary(), "Dynamic EmployeeType change");

        System.out.println("PASS delegation salary delegation");
    }

    private static void assertNear(double expected, double actual, String label) {
        if (Math.abs(expected - actual) > EPSILON) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }
}
