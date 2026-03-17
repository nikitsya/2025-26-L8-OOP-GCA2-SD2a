package com.supermarketstore.product;

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

class JdbcProductDaoTest {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket_store_system";
    private static final String DB_USER = "root";
    private static final String DB_PASS = System.getenv("TEST_DB_PASS");
    private static final String TEST_NAME_PATTERN = "TEST_%";

    static Product product1;
    static Product product2;

    static JdbcProductDao dao;

    @BeforeAll
    static void beforeAll() {
        if (DB_PASS == null || DB_PASS.isBlank()) fail("Set TEST_DB_PASS in Run Configuration");
        dao = new JdbcProductDao(DB_URL, DB_USER, DB_PASS);
        cleanupTestRows();

        // add test products to the database
        product1 = new Product(0, "TEST_cucumber", 0.65, false, null, 98);
        product2 = new Product(0, "TEST_cucumber", 0.70, true, 0.65, 126);
        dao.insertProduct(product1);
        dao.insertProduct(product2);
    }

    @AfterAll
    static void afterAll() {
        cleanupTestRows();
        dao = null;
    }

    private static void cleanupTestRows() {
        String sql = "DELETE FROM products WHERE name LIKE ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, TEST_NAME_PATTERN);
            statement.executeUpdate();
        } catch (SQLException e) {
            fail("Failed to cleanup TEST_ products: " + e.getMessage());
        }
    }

    @Test
    void getAllProducts() {
        List<Product> products = dao.getAllProducts();
        assertNotNull(products);
        assertFalse(products.isEmpty());
        long testCount = products.stream().filter(p -> p.getName().startsWith("TEST_")).count();
        assertEquals(2, testCount);

    }

    @Test
    void getProductById() {
        int id = product1.getProductId();
        Optional<Product> found = dao.getProductById(id);
        assertTrue(found.isPresent());
        assertEquals(product1, found.get());
    }

    @Test
    void getProductById_whenIdDoesNotExist_returnsEmpty() {
        Optional<Product> found = dao.getProductById(999999);
        assertTrue(found.isEmpty());
    }

    @Test
    void deleteProductById() {
        int id = product1.getProductId();
        assertTrue(dao.deleteProductById(id));
        assertFalse(dao.getProductById(id).isPresent());
    }

    @Test
    void insertProduct() {

    }

    @Test
    void insertProduct_whenProductIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> dao.insertProduct(null)
        );
        assertEquals("product is required", ex.getMessage());
    }

    @Test
    void updateProduct() {
    }

    @Test
    void findProductsByFilter() {
    }

    @Test
    void _url() {
    }

    @Test
    void _user() {
    }

    @Test
    void _pass() {
    }
}
