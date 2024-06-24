package hei.std22049.models;


import hei.std22049.models.utils.Shifting;

import java.time.LocalDate;

public record Scoring (Employee employee, LocalDate date, int hoursWorked, Shifting shifting) {
}
