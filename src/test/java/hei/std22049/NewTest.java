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

public class NewTest {
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
        calendar.addHoliday(LocalDate.of(2024,6,26));
    }

    @Test
    public void calculateHoursWorkOfRakoto(){
        TimeTrackingUtil timeTrackingOfRakoto = new TimeTrackingUtil(rakoto,scoringList,calendar);
        int totalHoursWorked = timeTrackingOfRakoto.calculateWeeklyHours();
        assertEquals(420,totalHoursWorked);
    }
    @Test
    public void calculateHoursWorkOfRabe(){
        TimeTrackingUtil timeTrackingOfRabe = new TimeTrackingUtil(rabe,scoringList,calendar);
        int totalHoursWorked = timeTrackingOfRabe.calculateWeeklyHours();
        assertEquals(588,totalHoursWorked);
    }

    @Test
    void calculateHolidayHoursAndSalaryOfRakoto(){
        TimeTrackingUtil timeTrackingOfRakoto = new TimeTrackingUtil(rakoto,scoringList,calendar);
        int holidayHours = timeTrackingOfRakoto.getHolidayWorkHours();
        double hourlyRate = 100000.0 / 70;
        double holidayGrossSalary = SalaryOperation.getGrossSalaryForHolidays(holidayHours,hourlyRate);
        double expectedSalary = 10 * hourlyRate * 1.5;
        assertEquals(10,holidayHours,"it should return 10 hours of holiday work");
        assertEquals(expectedSalary,holidayGrossSalary,"it should return the expected holiday salary");
    }

    @Test
    void calculateHolidayHoursAndSalaryOfRabe(){
        TimeTrackingUtil timeTrackingOfRabe = new TimeTrackingUtil(rabe,scoringList,calendar);
        int holidayHours = timeTrackingOfRabe.getHolidayWorkHours();
        double hourlyRate = 100000.0 / 98;
        double holidayGrossSalary = SalaryOperation.getGrossSalaryForHolidays(holidayHours,hourlyRate);
        double expectedSalary = 14 * hourlyRate * 1.5;
        assertEquals(14,holidayHours,"it should return 14 hours of holiday work");
        assertEquals(expectedSalary,holidayGrossSalary,"it should return the expected holiday salary");
    }
    @Test
    public void testCalculateGrossAndNetSalaryForRakoto() {

        // Calculate Rakoto's gross salary
        double grossSalaryRakoto = SalaryOperation.calculateGrossSalary(rakoto, scoringList, calendar);
        // Calculate Rakoto's net salary
        double netSalaryRakoto = SalaryOperation.calculateNetSalary(grossSalaryRakoto);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 100000.0 / 70;
        double sundayRate = 60 * hourlyRate * 1.4;
        double holidayRate = 10 * hourlyRate * 1.5;
        double expectedGrossSalary = Math.round((350 * hourlyRate  + sundayRate + holidayRate) * 100.0) / 100.0;
        double expectedNetSalary = expectedGrossSalary * 0.8;
        assertEquals(expectedGrossSalary, grossSalaryRakoto, "should return the same gross salary amount");
        assertEquals(expectedNetSalary,netSalaryRakoto,"should return the same net salary amount");
    }

    @Test
    public void testCalculateGrossAndNetSalaryForRabe() {
        // Calculate Rabe's gross salary
        double grossSalaryRabe = SalaryOperation.calculateGrossSalary(rabe, scoringList, calendar);
        // Calculate Rabe's net salary
        double netSalaryRabe = SalaryOperation.calculateNetSalary(grossSalaryRabe);

        // Check if the calculated gross salary is as expected
        double hourlyRate = 100000.0 / 98; // Weekly salary divided by regular hours
        double nightRate = 588 * hourlyRate * 1.3;
        double sundayRate = 84 * hourlyRate * 1.4;
        double holidayRate = 14 * hourlyRate * 1.5;
        double expectedGrossSalary = Math.round((nightRate + sundayRate + holidayRate) * 100.0) / 100.0;
        double expectdedNetSalary = expectedGrossSalary * 0.8;
        assertEquals(expectedGrossSalary, grossSalaryRabe,"should return the expected gross salary");
        assertEquals(expectdedNetSalary,netSalaryRabe,"should return the expected net salary");
    }
}
