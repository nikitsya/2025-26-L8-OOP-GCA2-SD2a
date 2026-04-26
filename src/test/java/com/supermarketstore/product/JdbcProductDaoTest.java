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

/**
 * Integration tests for {@link JdbcProductDao}.
 *
 * @author Nikita Smechik
 */
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
        product1 = new Product(0, "TEST_cucumber", 0.65, false, null, 98, null, null, null, 0);
        product2 = new Product(0, "TEST_cucumber", 0.70, true, 0.65, 126, null, null, null, 0);
        dao.insertProduct(product1);
        dao.insertProduct(product2);
    }

    @AfterAll
    static void afterAll() {
        dao = null;
        cleanupTestRows();
    }

    private static void cleanupTestRows() {
        String sql = "DELETE FROM supermarket_store_system.products WHERE name LIKE ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, TEST_NAME_PATTERN);
            statement.executeUpdate();
        } catch (SQLException e) {
            fail("Failed to cleanup TEST_ products: " + e.getMessage());
        }
    }

    @Test
    void constructor_whenUrlIsBlank_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new JdbcProductDao(" ", DB_USER, DB_PASS)
        );

        assertEquals("url is required", exception.getMessage());
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
    void getProductById_whenProductExists_returnsMetadataWithoutImageData() {
        Product inserted = dao.insertProduct(new Product(0, "TEST_MetadataOnly", 2.50, false, null, 20, new byte[]{5, 6, 7}, "metadata.jpeg", "image/jpeg", 3));

        Optional<Product> fetched = dao.getProductById(inserted.getProductId());

        assertTrue(fetched.isPresent());

        Product actual = fetched.get();
        assertAll(
                () -> assertEquals(inserted.getProductId(), actual.getProductId()),
                () -> assertEquals("TEST_MetadataOnly", actual.getName()),
                () -> assertEquals(2.50, actual.getPrice()),
                () -> assertFalse(actual.isOnSale()),
                () -> assertNull(actual.getDiscountPrice()),
                () -> assertEquals(20, actual.getStock()),
                () -> assertEquals("metadata.jpeg", actual.getFileName()),
                () -> assertEquals("image/jpeg", actual.getContentType()),
                () -> assertEquals(3, actual.getFileSize()),
                () -> assertNull(actual.getProductImage())
        );
    }

    @Test
    void getProductById_whenIdDoesNotExist_returnsEmpty() {
        Optional<Product> found = dao.getProductById(999999);
        assertTrue(found.isEmpty());
    }

    @Test
    void getProductById_whenIdIsNotPositive_returnsEmpty() {
        assertAll(
                () -> assertTrue(dao.getProductById(0).isEmpty()),
                () -> assertTrue(dao.getProductById(-1).isEmpty())
        );
    }

    @Test
    void deleteProductById() {
        int id = product1.getProductId();
        assertTrue(dao.deleteProductById(id));
        assertFalse(dao.getProductById(id).isPresent());
    }

    @Test
    void insertProduct() {
        Product toInsert = new Product(0, "TEST_insert_milk", 1.49, false, null, 15, null, null, null, 0);
        Product inserted = dao.insertProduct(toInsert);
        assertTrue(inserted.getProductId() > 0);
        assertEquals(toInsert, inserted);

        Optional<Product> productFromDb = dao.getProductById(inserted.getProductId());
        assertTrue(productFromDb.isPresent());
        assertEquals("TEST_insert_milk", productFromDb.get().getName());
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
        int id = product2.getProductId();
        Product toUpdate = new Product(333, "TEST_tomato", 0.35, false, null, 70, null, null, null, 0);
        Product updated = dao.updateProduct(id, toUpdate);
        assertEquals(toUpdate.getName(), updated.getName());
        assertEquals(toUpdate, updated);
    }

    @Test
    void updateProduct_whenProductIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> dao.updateProduct(1, null)
        );
        assertEquals("product is required", ex.getMessage());
    }

    @Test
    void findProductsByFilter() {
        List<Product> filtered = dao.findProductsByFilter(p -> p.getName().startsWith("TEST_") && p.getPrice() >= 0.70);
        assertFalse(filtered.isEmpty());
        assertTrue(filtered.stream().allMatch(p -> p.getName().startsWith("TEST_")));
        assertTrue(filtered.stream().allMatch(p -> p.getPrice() >= 0.70));
    }

    @Test
    void findProductsByFilter_whenFilterIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> dao.findProductsByFilter(null)
        );
        assertEquals("filter is required", ex.getMessage());
    }
}
