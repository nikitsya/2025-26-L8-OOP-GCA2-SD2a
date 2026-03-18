package com.supermarketstore.department;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    void insertDepartment_shouldPersistAndReturnMatchingDepartment() {
        Department newDepartment = new Department(0, "TEST_Bakery", 0, 2, 12000.0, 5, false);

        // Insert a new row and keep the generated id returned by the DAO.
        Department inserted = dao.insertDepartment(newDepartment);

        // Read the same row back from the database and verify the stored values.
        Optional<Department> fetched = dao.getDepartmentById(inserted.getDepartmentId());

        assertTrue(fetched.isPresent());

        Department actual = fetched.get();
        assertEquals(inserted.getDepartmentId(), actual.getDepartmentId());
        assertEquals("TEST_Bakery", actual.getName());
        assertEquals(0, actual.getFloor());
        assertEquals(2, actual.getZone());
        assertEquals(12000.0, actual.getBudget());
        assertEquals(5, actual.getEmployeeCount());
        assertFalse(actual.isRefrigerated());
    }

    @Test
    void deleteDepartment_shouldRemoveInsertedDepartment() {
        Department newDepartment = new Department(0, "TEST_DeleteBakery", 1, 4, 9000.0, 3, true);
        Department inserted = dao.insertDepartment(newDepartment);

        // Delete the row we just inserted and confirm it is no longer in the table.
        boolean deleted = dao.deleteDepartmentById(inserted.getDepartmentId());
        Optional<Department> fetched = dao.getDepartmentById(inserted.getDepartmentId());

        assertTrue(deleted);
        assertTrue(fetched.isEmpty());
    }

    @Test
    void updateDepartment_shouldPersistUpdatedValues() {
        Department original = new Department(0, "TEST_OriginalBakery", 0, 2, 12000.0, 5, false);
        Department inserted = dao.insertDepartment(original);

        Department changes = new Department(0, "TEST_UpdatedBakery", 1, 6, 15000.0, 8, true);

        // Update the stored row, then read it back to confirm the new values were saved.
        Department updated = dao.updateDepartment(inserted.getDepartmentId(), changes);
        Optional<Department> fetched = dao.getDepartmentById(inserted.getDepartmentId());

        assertTrue(fetched.isPresent());

        Department actual = fetched.get();
        assertEquals(inserted.getDepartmentId(), updated.getDepartmentId());
        assertEquals(inserted.getDepartmentId(), actual.getDepartmentId());
        assertEquals("TEST_UpdatedBakery", actual.getName());
        assertEquals(1, actual.getFloor());
        assertEquals(6, actual.getZone());
        assertEquals(15000.0, actual.getBudget());
        assertEquals(8, actual.getEmployeeCount());
        assertTrue(actual.isRefrigerated());
    }

    @Test
    void getDepartmentById_whenIdDoesNotExist_returnsEmpty() {
        Optional<Department> fetched = dao.getDepartmentById(999999);

        assertTrue(fetched.isEmpty());
    }
}
