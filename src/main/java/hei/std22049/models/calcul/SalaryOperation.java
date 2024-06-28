package hei.std22049.models.calcul;

import hei.std22049.models.Calendar;
import hei.std22049.models.Category;
import hei.std22049.models.Employee;
import hei.std22049.models.Scoring;
import hei.std22049.models.utils.TimeTrackingUtil;
import java.util.List;

public class SalaryOperation {

    public static double calculateGrossSalary(Employee employee, List<Scoring> scorings, Calendar calendar) {
        Category category = employee.getCategory();
        int regularHours = category.hoursPerWeek();
        double hourlyRate = getSalaryPerHour(category,regularHours);
        TimeTrackingUtil timeTracking = new TimeTrackingUtil(employee,scorings,calendar);
        // get all hours working for an employee
        int dayWorkHours = timeTracking.getDayWorkHours();
        int holidayWorkHours = timeTracking.getHolidayWorkHours();
        int nightWorkHours = timeTracking.getNightWorkHours();
        int sundayWorkHours = timeTracking.getSundayWorkHours();
        System.out.println("dayWorkHours:"+dayWorkHours+", holidayWorkHours:"+holidayWorkHours+", nightWorkHours:"+nightWorkHours+", sundayWorkHours:"+sundayWorkHours);

        // get all gross salaries
        double grossSalaryDays = getGrossSalaryForDayHours(dayWorkHours,  hourlyRate);
        double grossSalaryHolidays = getGrossSalaryForHolidays(holidayWorkHours, hourlyRate);
        double grossSalaryNight = getGrossSalaryForNightHours(nightWorkHours, hourlyRate);
        double grossSalarySunday = getGrossSalaryForSundayHours(sundayWorkHours, hourlyRate);
        System.out.println("day:"+grossSalaryDays+", holidays:"+grossSalaryHolidays+", night:"+grossSalaryNight+", sunday:"+grossSalarySunday);

        return Math.round((grossSalaryDays + grossSalaryHolidays + grossSalaryNight + grossSalarySunday) * 100.0) / 100.0;
    }

    public static double getSalaryPerHour(Category category, int regularHours){
        return category.salaryPerWeek() / regularHours;
    }

    private static double getGrossSalaryForDayHours(int dayHours, double hourlyRate) {
        return dayHours * hourlyRate;
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

}
