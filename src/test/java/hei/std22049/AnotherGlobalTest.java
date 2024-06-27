package hei.std22049;

import hei.std22049.models.*;
import hei.std22049.models.calcul.SalaryOperation;
import hei.std22049.models.utils.CategoryType;
import hei.std22049.models.utils.Shifting;
import hei.std22049.models.utils.TimeTrackingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnotherGlobalTest {
    private Employee rakoto;
    private Employee rabe;
    private List<Scoring> scoringList;
    private Calendar calendar;

    @BeforeEach
    public void setUp() {
        Category guardRakoto = new Category(CategoryType.guard, 70, 100000);
        Category guardRabe = new Category(CategoryType.guard, 98, 100000);

        Salary rakotoSalary = new Salary(100000);
        rakoto = new Employee("Rakoto", "Jean", LocalDate.of(1980, 5, 15), "12345", LocalDate.of(2010, 6, 1), null, rakotoSalary, guardRakoto);

        Salary rabeSalary = new Salary(100000);
        rabe = new Employee("Rabe", "Paul", LocalDate.of(1982, 8, 20), "12346", LocalDate.of(2012, 7, 1), null, rabeSalary, guardRabe);

        LocalDate startDate = LocalDate.of(2024, 5, 26);
        LocalDate endDate = LocalDate.of(2024, 7, 6);
        calendar = new Calendar(startDate,endDate);

        // Example scoring records for Rakoto and Rabe
        scoringList = new ArrayList<>();
        // Adding work records for 6 weeks
        LocalDate currentDate = LocalDate.of(2024, 5, 26);
        for (int i = 0; i < 42; i++) {
            scoringList.add(new Scoring(rakoto, currentDate.plusDays(i), 10, Shifting.DAY));
            scoringList.add(new Scoring(rabe, currentDate.plusDays(i), 14, Shifting.NIGHT));
        }
    }

    @Test
    public void testCalculateGrossSalaryForRakoto() {

        // Calculate Rakoto's gross salary
        double grossSalaryRakoto = SalaryOperation.calculateGrossSalary(rakoto, scoringList, calendar);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 100000.0 / 70;
        double sundayRate = 60 * hourlyRate * 1.4;
        double expectedGrossSalary = 360 * hourlyRate  + sundayRate;
        assertEquals(expectedGrossSalary, grossSalaryRakoto, 0.01);
    }

    @Test
    public void testCalculateGrossSalaryForRabe() {
        // Calculate Rabe's gross salary
        double grossSalaryRabe = SalaryOperation.calculateGrossSalary(rabe, scoringList, calendar);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 100000.0 / 98; // Weekly salary divided by regular hours
        double nightRate = 588 * hourlyRate * 1.3;
        double sundayRate = 84 * hourlyRate * 1.4;
        double expectedGrossSalary = nightRate + sundayRate;
        assertEquals(expectedGrossSalary, grossSalaryRabe);
    }

    @Test
    public void calculateHoursWorkOfRakoto(){
        int totalHoursWorked = TimeTrackingUtil.calculateWeeklyHours(rakoto,scoringList,calendar);
        assertEquals(420,totalHoursWorked);
    }
    @Test
    public void calculateHoursWorkOfRabe(){
        int totalHoursWorked = TimeTrackingUtil.calculateWeeklyHours(rabe,scoringList,calendar);
        assertEquals(588,totalHoursWorked);
    }

    @Test
    public void calculateSalaryOfRakotoPerHour(){
        Category category = rakoto.getCategory();
        double salary = SalaryOperation.getSalaryPerHour(category,category.hoursPerWeek());
        double rounded = Math.round(salary * 100.0) / 100.0;
        assertEquals(1428.57,rounded);
    }

    @Test
    public void calculateSalaryOfRabePerHour(){
        Category category = rabe.getCategory();
        double salary = SalaryOperation.getSalaryPerHour(category,category.hoursPerWeek()) * 1.3;
        double rounded = Math.round(salary * 100.0) / 100.0;
        assertEquals(1326.53,rounded);
    }
}
