package com.supermarketstore.department;

import java.util.List;

/**
 * Temporary manual runner.
 * Used only for local testing of Department JDBC operations.
 */
public class DepartmentMain {

    public static void main(String[] args) {

        JdbcDepartmentDao dao =
                new JdbcDepartmentDao(
                        "jdbc:mysql://localhost:3306/supermarket_store_system",
                        "root",
                        ""
                );

        List<Department> departments = dao.getAllDepartments();

        for (Department d : departments) {
            System.out.println(d);
        }
    }
}