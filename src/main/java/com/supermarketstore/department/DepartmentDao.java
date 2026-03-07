package com.supermarketstore.department;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface DepartmentDao {

    // Gets: all departments from the data store
    List<Department> getAllDepartments();

    // Gets: a department by its id
    Optional<Department> getDepartmentById(int id);

    // Deletes: a department by id
    boolean deleteDepartmentById(int id);

    // Inserts: a new department
    Department insertDepartment(Department department);

    // Updates: an existing department
    Department updateDepartment(int id, Department department);

    // Finds: departments matching a filter
    List<Department> findDepartmentsByFilter(Predicate<Department> filter);
}