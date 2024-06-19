package hei.std22049.models;

import java.time.LocalDate;

public final class Employee extends Person {
    private final String registrationNumber;
    private final LocalDate hireDate;
    private final LocalDate contractEnd;
    private final Salary salary;
    private final Category category;

    public Employee(String name, String firstname, LocalDate birthdate, String registrationNumber, LocalDate hireDate, LocalDate contractEnd, Salary salary, Category category) {
        super(name, firstname, birthdate);
        this.registrationNumber = registrationNumber;
        this.hireDate = hireDate;
        this.contractEnd = contractEnd;
        this.salary = salary;
        this.category = category;
    }
}
