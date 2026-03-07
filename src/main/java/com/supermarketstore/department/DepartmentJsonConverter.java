package com.supermarketstore.department;

import java.util.List;

public interface DepartmentJsonConverter {

    // Converts: a Department object to JSON
    String departmentToJson(Department entity);

    // Converts: JSON string to Department object
    Department departmentFromJson(String json);

    // Converts: a list of Departments to JSON
    String departmentListToJson(List<Department> list);
}