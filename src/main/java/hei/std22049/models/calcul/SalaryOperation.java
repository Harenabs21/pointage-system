package hei.std22049.models.calcul;

import hei.std22049.models.Calendar;
import hei.std22049.models.Category;
import hei.std22049.models.Employee;
import hei.std22049.models.Scoring;
import hei.std22049.models.utils.Shifting;
import hei.std22049.models.utils.TimeTrackingUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class SalaryOperation {

    public static double calculateGrossSalary(Employee employee, List<Scoring> scorings, LocalDate startDate, LocalDate endDate, Calendar calendar) {
        Category category = employee.getCategory();
        int regularHours = category.hoursPerWeek();
        double hourlyRate = getSalaryPerHour(category,regularHours);

        // get all hours working for an employee
        int totalHoursWorked = TimeTrackingUtil.calculateWeeklyHours(employee, scorings, startDate, endDate);
        int holidayWorkHours = getHolidayWorkHours(employee, scorings, calendar, startDate, endDate);
        int nightWorkHours = getNightWorkHours(employee, scorings, startDate, endDate);
        int sundayWorkHours = getSundayWorkHours(employee, scorings, startDate, endDate);
        int  totalHoursWorkedForRegularAndOvertime = totalHoursWorked - holidayWorkHours - nightWorkHours - sundayWorkHours;

        // get all gross salaries
        double grossSalaryRegular = getGrossSalaryRegular(totalHoursWorkedForRegularAndOvertime, regularHours, hourlyRate);
        double grossSalaryOvertime = getGrossSalaryWithOvertime(totalHoursWorkedForRegularAndOvertime, regularHours, hourlyRate);
        double grossSalaryHolidays = getGrossSalaryForHolidays(holidayWorkHours, hourlyRate);
        double grossSalaryNight = getGrossSalaryForNightHours(nightWorkHours, hourlyRate);
        double grossSalarySunday = getGrossSalaryForSundayHours(sundayWorkHours, hourlyRate);

        return grossSalaryRegular + grossSalaryOvertime + grossSalaryHolidays + grossSalaryNight + grossSalarySunday;
    }

    public static double getSalaryPerHour(Category category, int regularHours){
        return category.salaryPerWeek() / regularHours;
    }

    private static double getGrossSalaryRegular(int hoursWorked, int regularHours, double hourlyRate) {
        return Math.min(hoursWorked, regularHours) * hourlyRate;
    }

    private static double getGrossSalaryWithOvertime(int hoursWorked, int regularHours, double hourlyRate) {
        int overtimeHours = Math.max(0, hoursWorked - regularHours);
        int limitedOvertimeHours = Math.min(overtimeHours, 20);
        double grossSalary = 0.0;

        if (limitedOvertimeHours > 0) {
            int overtime30 = Math.min(limitedOvertimeHours, 8);
            int overtime50 = Math.max(0, limitedOvertimeHours - 8);

            grossSalary += overtime30 * hourlyRate * 1.3;
            grossSalary += Math.min(overtime50, 12) * hourlyRate * 1.5;
        }

        return grossSalary;
    }

    private static double getGrossSalaryForHolidays(int holidayWorkHours, double hourlyRate) {
        return holidayWorkHours * hourlyRate * 1.5; // Assuming 50% premium for holidays
    }

    private static double getGrossSalaryForNightHours(int nightWorkHours, double hourlyRate) {
        return nightWorkHours * hourlyRate * 1.3; // 30% premium for night hours
    }

    private static double getGrossSalaryForSundayHours(int sundayWorkHours, double hourlyRate) {
        return sundayWorkHours * hourlyRate * 1.4; // 40% premium for Sunday hours
    }

    private static int getHolidayWorkHours(Employee employee, List<Scoring> scorings, Calendar calendar, LocalDate startDate, LocalDate endDate) {
        int holidayWorkHours = 0;

        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && calendar.isHoliday(scoring.date())
                    && !scoring.date().isBefore(startDate) && !scoring.date().isAfter(endDate)) {
                holidayWorkHours += scoring.hoursWorked();
            }
        }

        return holidayWorkHours;
    }

    private static int getNightWorkHours(Employee employee, List<Scoring> scorings, LocalDate startDate, LocalDate endDate) {
        int nightWorkHours = 0;

        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && scoring.shifting() == Shifting.NIGHT
                    && !scoring.date().isBefore(startDate) && !scoring.date().isAfter(endDate)) {
                nightWorkHours += scoring.hoursWorked();
            }
        }

        return nightWorkHours;
    }

    private static int getSundayWorkHours(Employee employee, List<Scoring> scorings, LocalDate startDate, LocalDate endDate) {
        int sundayWorkHours = 0;

        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && scoring.date().getDayOfWeek() == DayOfWeek.SUNDAY
                    && !scoring.date().isBefore(startDate) && !scoring.date().isAfter(endDate)) {
                sundayWorkHours += scoring.hoursWorked();
            }
        }

        return sundayWorkHours;
    }
}
