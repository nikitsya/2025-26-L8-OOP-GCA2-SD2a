package com.supermarketstore.department;

import java.util.List;

/**
 * Defines JSON conversion operations for Department entities and department lists.
 */
public interface DepartmentJsonConverter {

    /**
     * Serialises a department to JSON.
     *
     * @param entity the department to serialise
     * @return the JSON representation of the department
     * @throws IllegalArgumentException if the department is null or cannot be serialised
     */
    String departmentToJson(Department entity);

    /**
     * Deserialises JSON into a department.
     *
     * @param json the JSON representation of a department
     * @return the department represented by the JSON input
     * @throws IllegalArgumentException if the JSON input is blank or cannot be deserialised
     */
    Department departmentFromJson(String json);

    /**
     * Serialises a list of departments to JSON.
     *
     * @param list the departments to serialise
     * @return the JSON representation of the department list
     * @throws IllegalArgumentException if the list is null or cannot be serialised
     */
    String departmentListToJson(List<Department> list);

    /**
     * Deserialises JSON into a list of departments.
     *
     * @param json the JSON representation of a department list
     * @return the departments represented by the JSON input
     * @throws IllegalArgumentException if the JSON input is blank or cannot be deserialised
     */
    List<Department> departmentListFromJson(String json);
}
