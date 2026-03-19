package com.supermarketstore.department;

import java.util.List;

/**
 * Temporary manual runner.
 * Used only for local testing of Department JDBC operations.
 */
public class DepartmentMain {

    public static void main(String[] args) {
        String dbPass = System.getenv("TEST_DB_PASS");

        if (dbPass == null || dbPass.isBlank()) {
            throw new IllegalStateException("Set TEST_DB_PASS before running DepartmentMain");
        }

        JdbcDepartmentDao dao =
                new JdbcDepartmentDao(
                        "jdbc:mysql://localhost:3306/supermarket_store_system",
                        "root",
                        dbPass
                );

        List<Department> departments = dao.getAllDepartments();
        List<Department> filtered = dao.findDepartmentsByFilter(department -> department.getFloor() == 1);

        System.out.println("All departments:");
        for (Department d : departments) {
            System.out.println(d);
        }

        System.out.println("Filtered departments:");
        for (Department d : filtered) {
            System.out.println(d);
        }
    }
}
