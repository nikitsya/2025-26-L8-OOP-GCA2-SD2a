package com.supermarketstore.department;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.sql.*;

public record JdbcDepartmentDao(String _url, String _user, String _pass) implements DepartmentDao {

    public JdbcDepartmentDao(String _url, String _user, String _pass) {
        if (_url == null || _url.isBlank()) throw new IllegalArgumentException("url is required");
        this._url = _url.trim();
        this._user = _user;
        this._pass = _pass;
    }

    // Opens: a new database connection using the configured JDBC credentials
    private Connection open() throws SQLException {
        return DriverManager.getConnection(_url, _user, _pass);
    }

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