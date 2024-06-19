package hei.std22049.models;

public class Salary {
    private final double grossSalary;
    private double netSalary;

    public Salary(double grossSalary){
        this.grossSalary = grossSalary;
        this.netSalary = calculateNetSalary(grossSalary);
    }

    private double calculateNetSalary(double salaireBrut){
        return salaireBrut * 0.8;
    }
}
