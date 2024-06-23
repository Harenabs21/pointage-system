package hei.std22049.models;

import lombok.Getter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Calendar{
    private Set<LocalDate> holidays;

    public Calendar(){
        this.holidays = new HashSet<>();
    }
    public void addHoliday(LocalDate date){
        holidays.add(date);
    }
    public boolean isHoliday(LocalDate date){
        return holidays.contains(date);
    }
}
