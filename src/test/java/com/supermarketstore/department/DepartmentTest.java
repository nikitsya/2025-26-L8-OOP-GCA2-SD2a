package com.supermarketstore.department;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DepartmentTest {

    @Test
    void shouldTrimDepartmentName() {
        Department department = new Department(1, "  Bakery  ", 0, 2, 12000.0, 5, false);

        assertEquals("Bakery", department.getName());
    }
}
