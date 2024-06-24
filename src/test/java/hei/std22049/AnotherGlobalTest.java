package hei.std22049;

import hei.std22049.models.*;
import hei.std22049.models.utils.CategoryType;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.List;

public class AnotherGlobalTest {
    private Employee rakoto;
    private Employee rabe;
    private List<Scoring> scoringList;
    private Calendar calendar;

    @BeforeEach
    void setUp(){
        // Creating categories with weekly hours and weekly salary
        Category guardCategory = new Category(CategoryType.guard, 56, 100000);

        // Creating employees with salary and category
        Salary rakotoSalary = new Salary(100000.0);
        rakoto = new Employee("Rakoto", "Jean", LocalDate.of(1980, 5, 15), "12345", LocalDate.of(2010, 6, 1), null, rakotoSalary, guardCategory);

        Salary rabeSalary = new Salary(100000.0);
        rabe = new Employee("Rabe", "Paul", LocalDate.of(1982, 8, 20), "12346", LocalDate.of(2012, 7, 1), null, rabeSalary, guardCategory);

        // Creating a calendar and adding holidays
        calendar = new Calendar();
    }
}
