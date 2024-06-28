package hei.std22049.models.utils;
import hei.std22049.models.Calendar;
import hei.std22049.models.Employee;
import hei.std22049.models.Scoring;
import lombok.AllArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@AllArgsConstructor
public class TimeTrackingUtil {
    private final Employee employee;
    private List<Scoring> scorings;
    private Calendar calendar;


    public  int calculateWeeklyHours() {
        int totalHours = 0;
        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && !scoring.date().isBefore(calendar.getBegin()) && !scoring.date().isAfter(calendar.getEnd())) {
                totalHours += scoring.hoursWorked();
            }
        }
        return totalHours;
    }

    public int getHolidayWorkHours() {
        int holidayWorkHours = 0;

        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && calendar.isHoliday(scoring.date())
                    && !scoring.date().isBefore(calendar.getBegin()) && !scoring.date().isAfter(calendar.getEnd())) {
                holidayWorkHours += scoring.hoursWorked();
            }
        }

        return holidayWorkHours;
    }

    public int getNightWorkHours() {
        int nightWorkHours = 0;

        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && scoring.shifting() == Shifting.NIGHT
                    && !scoring.date().isBefore(calendar.getBegin()) && !scoring.date().isAfter(calendar.getEnd())) {
                nightWorkHours += scoring.hoursWorked();
            }
        }

        return nightWorkHours;
    }

    public int getSundayWorkHours() {
        int sundayWorkHours = 0;

        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && scoring.date().getDayOfWeek() == DayOfWeek.SUNDAY
                    && !scoring.date().isBefore(calendar.getBegin()) && !scoring.date().isAfter(calendar.getEnd())) {
                sundayWorkHours += scoring.hoursWorked();
            }
        }

        return sundayWorkHours;
    }
}
