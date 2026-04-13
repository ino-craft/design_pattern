package grader.behavior.delegation;

import delegationProblem.Cat;
import delegationProblem.Dog;
import delegationProblem.Robot;
import delegationProblem.Sayable;
import delegationProblem.Worker;

public class DelegationBehaviorTest {
    public static void main(String[] args) {
        Worker[] workers = new Worker[] {
            new Dog("Buddy"),
            new Cat("Nabi"),
            new Robot()
        };

        for (Worker worker : workers) {
            worker.doWork();
            if (!(worker instanceof Sayable)) {
                throw new AssertionError(worker.getClass().getName() + " must be Sayable");
            }
            ((Sayable) worker).say();
        }

        System.out.println("PASS delegation behavior");
    }
}
