package hei.std22049.models;

import lombok.Getter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Calendar{
    private final LocalDate begin;
    private final LocalDate end;
    private Set<LocalDate> holidays;

    public Calendar(LocalDate begin, LocalDate end){
        this.begin = begin;
        this.end = end;
        this.holidays = new HashSet<>();
    }
    public void addHoliday(LocalDate date){
        holidays.add(date);
    }
    public boolean isHoliday(LocalDate date){
        return holidays.contains(date);
    }
}
