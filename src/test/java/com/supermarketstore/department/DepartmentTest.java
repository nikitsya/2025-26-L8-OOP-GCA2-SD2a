package com.supermarketstore.department;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link Department}.
 */
public class DepartmentTest {

    @Test
    void shouldTrimDepartmentName() {
        Department department = new Department(1, "  Bakery  ", 0, 2, 12000.0, 5, false);

        assertEquals("Bakery", department.getName());
    }

    @Test
    void shouldRejectBlankDepartmentName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Department(1, " ", 0, 2, 12000.0, 5, false)
        );
        assertEquals("Department name must not be null or blank", exception.getMessage());
    }

    @Test
    void shouldRejectNegativeDepartmentBudget() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Department(1, "Bakery", 0, 2, -1.0, 5, false)
        );

        assertEquals("Budget cannot be negative", exception.getMessage());
    }

    @Test
    void shouldRejectNegativeEmployeeCount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Department(1, "Bakery", 0, 2, 12000.0, -1, false)
        );

        assertEquals("Employee count cannot be negative", exception.getMessage());
    }

    @Test
    void constructorShouldRejectInvalidZone() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Department(1, "Bakery", 0, -1, 12000.0, 5, false)
        );

        assertEquals("Zone must be greater than 0", exception.getMessage());
    }
}
