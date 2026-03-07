package com.supermarketstore.department;

import java.util.ArrayList;
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
        String sql = "SELECT department_id, name, floor, zone, budget, employee_count, is_refrigerated FROM departments";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ArrayList<Department> out = new ArrayList<>();
            while (rs.next()) out.add(mapRow(rs));
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all departments", e);
        }
    }

    @Override
    public Optional<Department> getDepartmentById(int id) {
        if (id <= 0) return Optional.empty();

        String sql = "SELECT department_id, name, floor, zone, budget, employee_count, is_refrigerated FROM departments WHERE department_id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch department by id", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteDepartmentById(int id) {
        if (id <= 0) return false;

        String sql = "DELETE FROM departments WHERE department_id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete department by id", e);
        }
    }

    @Override
    public Department insertDepartment(Department department) {
        if (department == null) throw new IllegalArgumentException("department is required");

        String sql = "INSERT INTO departments (name, floor, zone, budget, employee_count, is_refrigerated) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, department.getName());
            ps.setInt(2, department.getFloor());
            ps.setInt(3, department.getZone());
            ps.setDouble(4, department.getBudget());
            ps.setInt(5, department.getEmployeeCount());
            ps.setBoolean(6, department.isRefrigerated());

            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new RuntimeException("Insert department failed");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);

                    return new Department(
                            generatedId,
                            department.getName(),
                            department.getFloor(),
                            department.getZone(),
                            department.getBudget(),
                            department.getEmployeeCount(),
                            department.isRefrigerated()
                    );
                }
            }

            throw new RuntimeException("Insert department failed, no ID returned");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert department", e);
        }
    }

    @Override
    public Department updateDepartment(int id, Department department) {
        if (id <= 0) throw new IllegalArgumentException("id must be greater than 0");
        if (department == null) throw new IllegalArgumentException("department is required");

        String sql = "UPDATE departments SET name = ?, floor = ?, zone = ?, budget = ?, employee_count = ?, is_refrigerated = ? WHERE department_id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, department.getName());
            ps.setInt(2, department.getFloor());
            ps.setInt(3, department.getZone());
            ps.setDouble(4, department.getBudget());
            ps.setInt(5, department.getEmployeeCount());
            ps.setBoolean(6, department.isRefrigerated());
            ps.setInt(7, id);

            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new RuntimeException("Update department failed or department not found");
            }

            return new Department(
                    id,
                    department.getName(),
                    department.getFloor(),
                    department.getZone(),
                    department.getBudget(),
                    department.getEmployeeCount(),
                    department.isRefrigerated()
            );

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update department", e);
        }
    }

    @Override
    public List<Department> findDepartmentsByFilter(Predicate<Department> filter) {
        return List.of();
    }

    // Maps: a single SQL ResultSet row to a Department object
    private Department mapRow(ResultSet resultSet) throws SQLException {
        int departmentId = resultSet.getInt("department_id");
        String name = resultSet.getString("name");
        int floor = resultSet.getInt("floor");
        int zone = resultSet.getInt("zone");
        double budget = resultSet.getDouble("budget");
        int employeeCount = resultSet.getInt("employee_count");
        boolean refrigerated = resultSet.getBoolean("is_refrigerated");

        return new Department(departmentId, name, floor, zone, budget, employeeCount, refrigerated);
    }
}