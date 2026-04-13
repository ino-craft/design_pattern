package delegationProblem;

public abstract class Worker {
    private double salary;
    private EmployeeType employeeType;

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void increaseSalary(double rate) {
        if (employeeType == null) {
            throw new IllegalStateException("Employee type not set");
        }
        salary = employeeType.calculateIncreasedSalary(salary, rate);
    }

	public abstract void doWork();
}
