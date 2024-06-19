package hei.std22049.models;

import hei.std22049.models.utils.CategoryType;
import lombok.Getter;

@Getter
public record Category(CategoryType name, int hoursPerWeek, double salaryPerWeek) {
}
