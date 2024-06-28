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
        int totalHoursWorked = timeTracking.calculateWeeklyHours();
        int holidayWorkHours = timeTracking.getHolidayWorkHours();
        int nightWorkHours = timeTracking.getNightWorkHours();
        int sundayWorkHours = timeTracking.getSundayWorkHours();
        int  totalHoursWorkedForRegular = Math.max(0,totalHoursWorked - holidayWorkHours - nightWorkHours - sundayWorkHours) ;
        System.out.println("totalHoursWorked:"+totalHoursWorked+", holidayWorkHours:"+holidayWorkHours+", nightWorkHours:"+nightWorkHours+", sundayWorkHours:"+sundayWorkHours+", totalHoursWorkedForRegular:"+totalHoursWorkedForRegular);

        // get all gross salaries
        double grossSalaryRegular = getGrossSalaryRegular(totalHoursWorkedForRegular,  hourlyRate);
        double grossSalaryHolidays = getGrossSalaryForHolidays(holidayWorkHours, hourlyRate);
        double grossSalaryNight = getGrossSalaryForNightHours(nightWorkHours, hourlyRate);
        double grossSalarySunday = getGrossSalaryForSundayHours(sundayWorkHours, hourlyRate);
        System.out.println("regular:"+grossSalaryRegular+", holidays:"+grossSalaryHolidays+", night:"+grossSalaryNight+", sunday:"+grossSalarySunday);

        return Math.round((grossSalaryRegular + grossSalaryHolidays + grossSalaryNight + grossSalarySunday) * 100.0) / 100.0;
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

}
