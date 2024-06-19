package hei.std22049.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public sealed class Person permits Employee{
    private final String name;
    private final String firstname;
    private final LocalDate birthdate;
}
