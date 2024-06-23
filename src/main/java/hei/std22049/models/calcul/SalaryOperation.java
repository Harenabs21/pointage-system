package hei.std22049.models.calcul;

import hei.std22049.models.Calendar;
import hei.std22049.models.Category;
import hei.std22049.models.Employee;

import java.time.LocalDate;

public class SalaryOperation {

    public static double calculateGrossSalary(Employee employee, int hoursWorked, Calendar calendar) {
        Category category = employee.getCategory();
        int regularHours = category.hoursPerWeek();
        double hourlyRate = category.salaryPerWeek() / regularHours;
        int overtimeHours = Math.max(0, hoursWorked - regularHours);
        double grossSalary = 0.0;

        // Calculate regular hours
        grossSalary += Math.min(hoursWorked, regularHours) * hourlyRate;

        // Calculate overtime hours
        if (overtimeHours > 0) {
            int overtime30 = Math.min(overtimeHours, 8);
            int overtime50 = overtimeHours - overtime30;
            grossSalary += overtime30 * hourlyRate * 1.3;
            grossSalary += overtime50 * hourlyRate * 1.5;
        }

        // Check for holidays
        for (LocalDate date : calendar.getHolidays()) {
            // If the employee worked on a holiday, add appropriate premiums
            // Assuming the hours worked on holidays are included in the total hours worked
            grossSalary += hoursWorked * hourlyRate * 0.5; // Example: 50% premium for holidays
        }

        return grossSalary;
    }
}
