package hei.std22049.models.calcul;

import hei.std22049.models.Calendar;
import hei.std22049.models.Category;
import hei.std22049.models.Employee;
import hei.std22049.models.Scoring;
import hei.std22049.models.utils.Shifting;
import hei.std22049.models.utils.TimeTrackingUtil;

import java.time.DayOfWeek;
import java.util.List;

public class SalaryOperation {

    public static double calculateGrossSalary(Employee employee, List<Scoring> scorings, Calendar calendar) {
        Category category = employee.getCategory();
        int regularHours = category.hoursPerWeek();
        double hourlyRate = getSalaryPerHour(category,regularHours);

        // get all hours working for an employee
        int totalHoursWorked = TimeTrackingUtil.calculateWeeklyHours(employee, scorings, calendar);
        int holidayWorkHours = getHolidayWorkHours(employee, scorings, calendar);
        int nightWorkHours = getNightWorkHours(employee, scorings, calendar);
        int sundayWorkHours = getSundayWorkHours(employee, scorings, calendar);
        int  totalHoursWorkedForRegular = Math.max(0,totalHoursWorked - holidayWorkHours - nightWorkHours - sundayWorkHours) ;
        System.out.println("totalHoursWorked:"+totalHoursWorked+", holidayWorkHours:"+holidayWorkHours+", nightWorkHours:"+nightWorkHours+", sundayWorkHours:"+sundayWorkHours+", totalHoursWorkedForRegular:"+totalHoursWorkedForRegular);

        // get all gross salaries
        double grossSalaryRegular = getGrossSalaryRegular(totalHoursWorkedForRegular,  hourlyRate);
        double grossSalaryHolidays = getGrossSalaryForHolidays(holidayWorkHours, hourlyRate);
        double grossSalaryNight = getGrossSalaryForNightHours(nightWorkHours, hourlyRate);
        double grossSalarySunday = getGrossSalaryForSundayHours(sundayWorkHours, hourlyRate);
        System.out.println("regular:"+grossSalaryRegular+", holidays:"+grossSalaryHolidays+", night:"+grossSalaryNight+", sunday:"+grossSalarySunday);

        return grossSalaryRegular + grossSalaryHolidays + grossSalaryNight + grossSalarySunday;
    }

    public static double getSalaryPerHour(Category category, int regularHours){
        return category.salaryPerWeek() / regularHours;
    }

    private static double getGrossSalaryRegular(int hoursWorked, double hourlyRate) {
        return hoursWorked * hourlyRate;
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

    private static int getHolidayWorkHours(Employee employee, List<Scoring> scorings, Calendar calendar) {
        int holidayWorkHours = 0;

        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && calendar.isHoliday(scoring.date())
                    && !scoring.date().isBefore(calendar.getBegin()) && !scoring.date().isAfter(calendar.getEnd())) {
                holidayWorkHours += scoring.hoursWorked();
            }
        }

        return holidayWorkHours;
    }

    private static int getNightWorkHours(Employee employee, List<Scoring> scorings, Calendar calendar) {
        int nightWorkHours = 0;

        for (Scoring scoring : scorings) {
            if (scoring.employee().equals(employee) && scoring.shifting() == Shifting.NIGHT
                    && !scoring.date().isBefore(calendar.getBegin()) && !scoring.date().isAfter(calendar.getEnd())) {
                nightWorkHours += scoring.hoursWorked();
            }
        }

        return nightWorkHours;
    }

    private static int getSundayWorkHours(Employee employee, List<Scoring> scorings, Calendar calendar) {
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
