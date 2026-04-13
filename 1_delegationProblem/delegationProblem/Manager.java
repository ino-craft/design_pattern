package delegationProblem;

public class Manager implements EmployeeType {
    public double calculateIncreasedSalary(double salary, double rate) {
        return (salary + 10.0) * (1.0 + rate / 100.0);
    }
}
