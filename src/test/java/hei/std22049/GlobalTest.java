package hei.std22049;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hei.std22049.models.*;
import hei.std22049.models.calcul.SalaryOperation;
import hei.std22049.models.utils.CategoryType;
import hei.std22049.models.utils.Shifting;
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
        calendar.addHoliday(LocalDate.of(2024, 6, 17)); // Adding June 17 as a holiday
        calendar.addHoliday(LocalDate.of(2024, 6, 25)); // Adding June 25 as a holiday
        calendar.addHoliday(LocalDate.of(2024, 6, 26)); // Adding June 26 as a holiday

        // Example scoring records for Rakoto and Rabe
        scorings = new ArrayList<>();
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 15), 8, Shifting.DAY));
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 16), 8, Shifting.DAY));
        scorings.add(new Scoring(rabe, LocalDate.of(2024, 6, 15), 8, Shifting.NIGHT));
        scorings.add(new Scoring(rabe, LocalDate.of(2024, 6, 16), 8, Shifting.NIGHT));
    }

    @Test
    public void testCalculateGrossSalaryWithoutOvertimeOrHolidays() {
        // Define the period for the week
        LocalDate startDate = LocalDate.of(2024, 6, 14);
        LocalDate endDate = LocalDate.of(2024, 6, 20);

        // Calculate Rakoto's gross salary
        double grossSalaryRakoto = SalaryOperation.calculateGrossSalary(rakoto, scorings, startDate, endDate, calendar);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 110000.0 / 56; // Weekly salary divided by regular hours
        double expectedGrossSalary = 16 * hourlyRate; // 16 hours worked
        assertEquals(expectedGrossSalary, grossSalaryRakoto);
    }

    @Test
    public void testCalculateGrossSalaryWithOvertime() {
        // Adding more hours to Rakoto's scoring
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 18), 12, Shifting.DAY)); // 12 hours of work on this day
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 19), 12, Shifting.DAY)); // 12 hours of work on this day

        // Define the period for the week
        LocalDate startDate = LocalDate.of(2024, 6, 14);
        LocalDate endDate = LocalDate.of(2024, 6, 20);

        // Calculate Rakoto's gross salary
        double grossSalaryRakoto = SalaryOperation.calculateGrossSalary(rakoto, scorings, startDate, endDate, calendar);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 110000.0 / 56;
        double expectedGrossSalary = (16 * hourlyRate) + (8 * hourlyRate * 1.3) + (4 * hourlyRate * 1.5); // 16 regular hours + 8 overtime hours at 130% + 4 overtime hours at 150%
        assertEquals(expectedGrossSalary, grossSalaryRakoto);
    }

    @Test
    public void testCalculateGrossSalaryWithNightWork() {
        // Adding night hours to Rakoto's scoring
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 18), 8, Shifting.NIGHT)); // 8 hours of night work on this day
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 19), 8, Shifting.NIGHT)); // 8 hours of night work on this day

        // Define the period for the week
        LocalDate startDate = LocalDate.of(2024, 6, 14);
        LocalDate endDate = LocalDate.of(2024, 6, 20);

        // Calculate Rakoto's gross salary
        double grossSalaryRakoto = SalaryOperation.calculateGrossSalary(rakoto, scorings, startDate, endDate, calendar);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 110000.0 / 56;
        double expectedGrossSalary = (16 * hourlyRate) + (16 * hourlyRate * 1.3); // 16 regular hours + 16 night hours at 130%
        assertEquals(expectedGrossSalary, grossSalaryRakoto);
    }

    @Test
    public void testCalculateGrossSalaryWithHolidayWork() {
        // Adding holiday hours to Rakoto's scoring
        scorings.add(new Scoring(rakoto, LocalDate.of(2024, 6, 17), 8, Shifting.DAY)); // 8 hours of work on a holiday

        // Define the period for the week
        LocalDate startDate = LocalDate.of(2024, 6, 14);
        LocalDate endDate = LocalDate.of(2024, 6, 20);

        // Calculate Rakoto's gross salary
        double grossSalaryRakoto = SalaryOperation.calculateGrossSalary(rakoto, scorings, startDate, endDate, calendar);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 110000.0 / 56;
        double expectedGrossSalary = (16 * hourlyRate) + (8 * hourlyRate * 1.5); // 16 regular hours + 8 holiday hours at 150%
        assertEquals(expectedGrossSalary, grossSalaryRakoto);
    }
}
