package com.supermarketstore.department;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;

class JdbcDepartmentDaoTest {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket_store_system";
    private static final String DB_USER = "root";
    private static final String DB_PASS = System.getenv("TEST_DB_PASS");
    private static final String TEST_NAME_PATTERN = "TEST_%";

    static JdbcDepartmentDao dao;

    @BeforeAll
    static void beforeAll() {
        if (DB_PASS == null || DB_PASS.isBlank()) {
            fail("Set TEST_DB_PASS in Run Configuration");
        }

        dao = new JdbcDepartmentDao(DB_URL, DB_USER, DB_PASS);

        // Keep the database predictable before DAO tests run.
        cleanupTestRows();
    }

    @AfterAll
    static void afterAll() {
        // Remove any rows created by DAO tests so reruns start cleanly.
        cleanupTestRows();
        dao = null;
    }

    private static void cleanupTestRows() {
        String sql = "DELETE FROM departments WHERE name LIKE ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, TEST_NAME_PATTERN);
            statement.executeUpdate();
        } catch (SQLException e) {
            fail("Failed to cleanup TEST_ departments: " + e.getMessage());
        }
    }
}
