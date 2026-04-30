package com.supermarketstore.department;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Data Access Object interface for Department entities.
 * Defines CRUD operations for accessing departments stored in the database.
 *
 * @author Hanna Bokariuk
 */
public interface DepartmentDao {

    /**
     * Retrieves all departments without loading image bytes.
     *
     * @return all departments stored in the data source
     */
    List<Department> getAllDepartments();

    /**
     * Retrieves one department without loading image bytes.
     *
     * @param id the department identifier
     * @return the matching department, or an empty optional when no department exists
     */
    Optional<Department> getDepartmentById(int id);

    /**
     * Deletes one department.
     *
     * @param id the department identifier
     * @return true when a department was deleted, otherwise false
     */
    boolean deleteDepartmentById(int id);

    /**
     * Inserts a new department and returns the stored entity with its generated identifier.
     *
     * @param department the department to insert
     * @return the inserted department with its generated identifier
     * @throws IllegalArgumentException if the department is null
     */
    Department insertDepartment(Department department);

    /**
     * Updates an existing department.
     *
     * @param id         the department identifier
     * @param department the replacement department values
     * @return the updated department
     * @throws IllegalArgumentException if the id or department is invalid
     */
    Department updateDepartment(int id, Department department);

    /**
     * Filters departments in memory using the supplied predicate.
     *
     * @param filter the predicate used to select departments
     * @return departments that match the predicate
     * @throws IllegalArgumentException if the filter is null
     */
    List<Department> findDepartmentsByFilter(Predicate<Department> filter);

    /**
     * Retrieves one department including its image bytes.
     *
     * @param id the department identifier
     * @return the matching department with image data, or an empty optional when no department exists
     */
    Optional<Department> getDepartmentImageById(int id);

}
