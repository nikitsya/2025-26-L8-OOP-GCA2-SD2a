package com.supermarketstore.department;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        Department newDepartment = new Department(0, "TEST_Bakery", 0, 2, 12000.0, 5, false, "bakery.jpg", "image/jpeg", 3, new byte[]{1, 2, 3});

        // Insert a new row and keep the generated id returned by the DAO.
        Department inserted = dao.insertDepartment(newDepartment);

        // Read the same row back from the database and verify the stored values.
        Optional<Department> fetched = dao.getDepartmentImageById(inserted.getDepartmentId());

        assertTrue(fetched.isPresent());

        Department actual = fetched.get();
        assertEquals(inserted.getDepartmentId(), actual.getDepartmentId());
        assertEquals("TEST_Bakery", actual.getName());
        assertEquals(0, actual.getFloor());
        assertEquals(2, actual.getZone());
        assertEquals(12000.0, actual.getBudget());
        assertEquals(5, actual.getEmployeeCount());
        assertFalse(actual.isRefrigerated());
        assertEquals("bakery.jpg", actual.getFileName());
        assertEquals("image/jpeg", actual.getContentType());
        assertEquals(3, actual.getFileSize());
        assertArrayEquals(new byte[]{1, 2, 3}, actual.getDepartmentImage());
    }

    @Test
    void deleteDepartment_shouldRemoveInsertedDepartment() {
        Department newDepartment = new Department(0, "TEST_DeleteBakery", 1, 4, 9000.0, 3, true, "delete-bakery.jpg", "image/jpeg", 3, new byte[]{4, 5, 6});
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

    @Test
    void findDepartmentsByFilter_shouldReturnOnlyMatchingDepartments() {
        Department lowBudget = new Department(0, "TEST_FilterBakery", 0, 2, 7000.0, 4, false);
        Department highBudget = new Department(0, "TEST_FilterFrozen", 1, 5, 18000.0, 7, true);

        dao.insertDepartment(lowBudget);
        dao.insertDepartment(highBudget);

        // The filter should keep only TEST_ departments that meet the budget rule.
        List<Department> filtered = dao.findDepartmentsByFilter(
                department -> department.getName().startsWith("TEST_") && department.getBudget() >= 10000.0
        );

        assertFalse(filtered.isEmpty());
        assertTrue(filtered.stream().allMatch(department -> department.getName().startsWith("TEST_")));
        assertTrue(filtered.stream().allMatch(department -> department.getBudget() >= 10000.0));
    }
}
