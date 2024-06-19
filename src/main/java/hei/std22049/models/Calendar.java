package hei.std22049.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Calendar{
    private Set<LocalDate> holidays;

    public Calendar(){
        this.holidays = new HashSet<>();
    }
    public void ajouterJoursFeries(LocalDate date){
        holidays.add(date);
    }
}
