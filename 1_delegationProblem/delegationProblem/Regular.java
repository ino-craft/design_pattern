package delegationProblem;

public class Regular implements EmployeeType {
    public double calculateIncreasedSalary(double salary, double rate) {
        return salary * (1.0 + rate / 100.0);
    }
}
