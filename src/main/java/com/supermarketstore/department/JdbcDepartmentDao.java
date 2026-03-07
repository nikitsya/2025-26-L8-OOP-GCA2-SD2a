package com.supermarketstore.department;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class JdbcDepartmentDao implements DepartmentDao {
    @Override
    public List<Department> getAllDepartments() {
        return List.of();
    }

    @Override
    public Optional<Department> getDepartmentById(int id) {
        return Optional.empty();
    }

    @Override
    public boolean deleteDepartmentById(int id) {
        return false;
    }

    @Override
    public Department insertDepartment(Department department) {
        return null;
    }

    @Override
    public Department updateDepartment(int id, Department department) {
        return null;
    }

    @Override
    public List<Department> findDepartmentsByFilter(Predicate<Department> filter) {
        return List.of();
    }
}