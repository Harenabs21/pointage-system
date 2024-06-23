package hei.std22049.models.utils;
import hei.std22049.models.Employee;
import hei.std22049.models.Scoring;

import java.time.LocalDate;
import java.util.List;

public class TimeTrackingUtil {

    public static int calculateWeeklyHours(Employee employee, List<Scoring> scorings, LocalDate startDate, LocalDate endDate) {
        int totalHours = 0;
        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && !scoring.date().isBefore(startDate) && !scoring.date().isAfter(endDate)) {
                totalHours += scoring.hoursWorked();
            }
        }
        return totalHours;
    }
}
