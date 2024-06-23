package hei.std22049;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hei.std22049.models.*;
import hei.std22049.models.calcul.SalaryOperation;
import hei.std22049.models.utils.CategoryType;
import hei.std22049.models.utils.TimeTrackingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GlobalTest {
    private Employee rakoto;
    private Employee rabe;
    private List<Scoring> scorings;
    private Calendar calendar;

    @BeforeEach
    public void setUp() {
        // Creating categories with weekly hours and weekly salary
        Category guardCategory = new Category(CategoryType.guard, 56, 110000);

        // Creating employees with salary and category
        Salary rakotoSalary = new Salary(110000);
        rakoto = new Employee("Rakoto", "Jean", LocalDate.of(1980, 5, 15), "12345", LocalDate.of(2010, 6, 1), null, rakotoSalary, guardCategory);

        Salary rabeSalary = new Salary(110000);
        rabe = new Employee("Rabe", "Paul", LocalDate.of(1982, 8, 20), "12346", LocalDate.of(2012, 7, 1), null, rabeSalary, guardCategory);

        // Creating a calendar and adding holidays
        calendar = new Calendar();
        calendar.addHoliday(LocalDate.of(2024, 6, 17));
        calendar.addHoliday(LocalDate.of(2024, 6, 25));
        calendar.addHoliday(LocalDate.of(2024, 6, 26));

        // Example scoring records for Rakoto and Rabe
        scorings = new ArrayList<>();
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 15), 8));
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 16), 8));
        scorings.add(new Scoring(rabe, LocalDate.of(2024, 6, 15), 8));
        scorings.add(new Scoring(rabe, LocalDate.of(2024, 6, 16), 8));
    }

    @Test
    public void testCalculateGrossSalaryWithoutOvertimeOrHolidays() {
        // Define the period for the week
        LocalDate startDate = LocalDate.of(2024, 6, 14);
        LocalDate endDate = LocalDate.of(2024, 6, 20);

        // Calculate the total hours worked by Rakoto for the week
        int totalHoursRakoto = TimeTrackingUtil.calculateWeeklyHours(rakoto, scorings, startDate, endDate);

        // Calculate Rakoto's gross salary
        double grossSalaryRakoto = SalaryOperation.calculateGrossSalary(rakoto, totalHoursRakoto, calendar);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 110000.0 / 56; // Weekly salary divided by regular hours
        double expectedGrossSalary = 16 * hourlyRate; // Regular hours, no overtime (16 hours worked)
        assertEquals(expectedGrossSalary, grossSalaryRakoto);
    }

    @Test
    public void testCalculateGrossSalaryWithOvertime() {
        // Adding more hours to Rakoto's scoring
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 18), 10)); // 10 hours of work on this day

        // Define the period for the week
        LocalDate startDate = LocalDate.of(2024, 6, 14);
        LocalDate endDate = LocalDate.of(2024, 6, 20);

        // Calculate the total hours worked by Rakoto for the week
        int totalHoursRakoto = TimeTrackingUtil.calculateWeeklyHours(rakoto, scorings, startDate, endDate);

        // Calculate Rakoto's gross salary
        double grossSalaryRakoto = SalaryOperation.calculateGrossSalary(rakoto, totalHoursRakoto, calendar);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 110000.0 / 56;
        double expectedGrossSalary = (8 + 8 + 10) * hourlyRate; // 26 hours including overtime
        assertEquals(expectedGrossSalary, grossSalaryRakoto);
    }

    @Test
    public void testCalculateGrossSalaryWithHolidays() {
        // Adding a holiday scoring for Rakoto
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 17), 8)); // 8 hours of work on a holiday

        // Define the period for the week
        LocalDate startDate = LocalDate.of(2024, 6, 14);
        LocalDate endDate = LocalDate.of(2024, 6, 20);

        // Calculate the total hours worked by Rakoto for the week
        int totalHoursRakoto = TimeTrackingUtil.calculateWeeklyHours(rakoto, scorings, startDate, endDate);

        // Calculate Rakoto's gross salary
        double grossSalaryRakoto = SalaryOperation.calculateGrossSalary(rakoto, totalHoursRakoto, calendar);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 110000.0 / 56;
        double expectedGrossSalary = (8 + 8 + 8) * hourlyRate + 8 * hourlyRate * 0.5; // Regular hours plus holiday premium
        assertEquals(expectedGrossSalary, grossSalaryRakoto);
    }
}
