package hei.std22049.models.utils;
import hei.std22049.models.Calendar;
import hei.std22049.models.Employee;
import hei.std22049.models.Scoring;

import java.util.List;

public class TimeTrackingUtil {

    public static int calculateWeeklyHours(Employee employee, List<Scoring> scorings, Calendar calendar) {
        int totalHours = 0;
        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && !scoring.date().isBefore(calendar.getBegin()) && !scoring.date().isAfter(calendar.getEnd())) {
                totalHours += scoring.hoursWorked();
            }
        }
        return totalHours;
    }
}
