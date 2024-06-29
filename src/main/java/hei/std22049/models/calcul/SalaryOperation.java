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
        int regularWorkHours = Math.max(0,dayWorkHours - holidayWorkHours - nightWorkHours - sundayWorkHours);
        System.out.println("regularWorkHours:"+regularWorkHours+", holidayWorkHours:"+holidayWorkHours+", nightWorkHours:"+nightWorkHours+", sundayWorkHours:"+sundayWorkHours);

        // get all gross salaries
        double grossSalaryDays = getGrossSalaryForDayHours(regularWorkHours,  hourlyRate);
        double grossSalaryHolidays = getGrossSalaryForHolidays(holidayWorkHours, hourlyRate);
        double grossSalaryNight = getGrossSalaryForNightHours(nightWorkHours, hourlyRate);
        double grossSalarySunday = getGrossSalaryForSundayHours(sundayWorkHours, hourlyRate);
        System.out.println("day:"+grossSalaryDays+", holidays:"+grossSalaryHolidays+", night:"+grossSalaryNight+", sunday:"+grossSalarySunday);

        return Math.round((grossSalaryDays + grossSalaryHolidays + grossSalaryNight + grossSalarySunday) * 100.0) / 100.0;
    }

    public static double getSalaryPerHour(Category category, int regularHours){
        return category.salaryPerWeek() / regularHours;
    }

    public static double getGrossSalaryForDayHours(int dayHours, double hourlyRate) {
        return Math.max(0,dayHours * hourlyRate);
    }

    public static double getGrossSalaryForHolidays(int holidayWorkHours, double hourlyRate) {
        return holidayWorkHours * hourlyRate * 1.5; // Assuming 50% premium for holidays
    }

    public static double getGrossSalaryForNightHours(int nightWorkHours, double hourlyRate) {
        return nightWorkHours * hourlyRate * 1.3; // 30% premium for night hours
    }

    public static double getGrossSalaryForSundayHours(int sundayWorkHours, double hourlyRate) {
        return sundayWorkHours * hourlyRate * 1.4; // 40% premium for Sunday hours
    }

    public static double calculateNetSalary(double grossSalary){
        return  grossSalary * 0.8;
    }

}
