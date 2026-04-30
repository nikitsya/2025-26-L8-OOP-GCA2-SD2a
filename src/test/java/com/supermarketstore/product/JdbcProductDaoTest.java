package com.supermarketstore.product;

import org.junit.jupiter.api.*;

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

    private static JdbcProductDao dao;

    @BeforeAll
    static void beforeAll() {
        if (DB_PASS == null || DB_PASS.isBlank()) {
            fail("Set TEST_DB_PASS in Run Configuration");
        }

        dao = new JdbcProductDao(DB_URL, DB_USER, DB_PASS);
    }

    @BeforeEach
    void setUp() {
        cleanupTestRows();
    }

    @AfterEach
    void tearDown() {
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
    void getAllProducts_returnsInsertedTestProductsWithoutImageData() {
        Product first = dao.insertProduct(new Product(0, "TEST_GetAllApples", 1.20, false, null, 12, new byte[]{1, 2}, "apples.jpeg", "image/jpeg", 2));
        Product second = dao.insertProduct(new Product(0, "TEST_GetAllPears", 1.40, true, 1.10, 8, new byte[]{3, 4}, "pears.jpeg", "image/jpeg", 2));

        List<Product> products = dao.getAllProducts();

        Optional<Product> fetchedFirst = products.stream()
                .filter(product -> product.getProductId() == first.getProductId())
                .findFirst();
        Optional<Product> fetchedSecond = products.stream()
                .filter(product -> product.getProductId() == second.getProductId())
                .findFirst();

        assertAll(
                () -> assertTrue(fetchedFirst.isPresent()),
                () -> assertTrue(fetchedSecond.isPresent()),
                () -> assertNull(fetchedFirst.orElseThrow().getProductImage()),
                () -> assertNull(fetchedSecond.orElseThrow().getProductImage())
        );
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
    void getProductImageById_whenProductExists_returnsImageBytesAndMetadata() {
        byte[] image = {9, 8, 7, 6};
        Product inserted = dao.insertProduct(new Product(0, "TEST_ImageRetrieval", 3.75, true, 2.99, 14, image, "image-retrieval.jpeg", "image/jpeg", image.length));

        Optional<Product> fetched = dao.getProductImageById(inserted.getProductId());

        assertTrue(fetched.isPresent());

        Product actual = fetched.get();
        assertAll(
                () -> assertEquals(inserted.getProductId(), actual.getProductId()),
                () -> assertEquals("TEST_ImageRetrieval", actual.getName()),
                () -> assertEquals("image-retrieval.jpeg", actual.getFileName()),
                () -> assertEquals("image/jpeg", actual.getContentType()),
                () -> assertEquals(image.length, actual.getFileSize()),
                () -> assertArrayEquals(image, actual.getProductImage())
        );
    }

    @Test
    void getProductImageById_whenIdDoesNotExist_returnsEmpty() {
        Optional<Product> fetched = dao.getProductImageById(999999);

        assertTrue(fetched.isEmpty());
    }

    @Test
    void getProductImageById_whenIdIsNotPositive_returnsEmpty() {
        assertAll(
                () -> assertTrue(dao.getProductImageById(0).isEmpty()),
                () -> assertTrue(dao.getProductImageById(-1).isEmpty())
        );
    }

    @Test
    void insertProduct_shouldPersistProductAndGeneratedId() {
        Product toInsert = new Product(0, "TEST_InsertMilk", 1.49, false, null, 15, null, null, null, 0);

        Product inserted = dao.insertProduct(toInsert);
        Optional<Product> fetched = dao.getProductById(inserted.getProductId());

        assertTrue(fetched.isPresent());

        Product actual = fetched.get();
        assertAll(
                () -> assertTrue(inserted.getProductId() > 0),
                () -> assertEquals(inserted.getProductId(), actual.getProductId()),
                () -> assertEquals("TEST_InsertMilk", actual.getName()),
                () -> assertEquals(1.49, actual.getPrice()),
                () -> assertFalse(actual.isOnSale()),
                () -> assertNull(actual.getDiscountPrice()),
                () -> assertEquals(15, actual.getStock())
        );
    }

    @Test
    void insertProduct_whenProductHasImage_persistsImageMetadataAndBytes() {
        byte[] image = {1, 3, 5, 7};
        Product toInsert = new Product(0, "TEST_InsertImage", 4.99, false, null, 6, image, "insert-image.jpeg", "image/jpeg", image.length);

        Product inserted = dao.insertProduct(toInsert);
        Optional<Product> fetched = dao.getProductImageById(inserted.getProductId());

        assertTrue(fetched.isPresent());

        Product actual = fetched.get();
        assertAll(
                () -> assertEquals("insert-image.jpeg", actual.getFileName()),
                () -> assertEquals("image/jpeg", actual.getContentType()),
                () -> assertEquals(image.length, actual.getFileSize()),
                () -> assertArrayEquals(image, actual.getProductImage())
        );
    }

    @Test
    void insertProduct_whenProductIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                dao.insertProduct(null)
        );

        assertEquals("product is required", exception.getMessage());
    }

    @Test
    void updateProduct_shouldPersistUpdatedValuesAndImage() {
        Product inserted = dao.insertProduct(new Product(0, "TEST_UpdateOriginal", 2.30, false, null, 9, new byte[]{1, 1}, "original.jpeg", "image/jpeg", 2));
        byte[] updatedImage = {2, 4, 6};
        Product changes = new Product(0, "TEST_UpdateChanged", 2.80, true, 2.10, 18, updatedImage, "updated.jpeg", "image/jpeg", updatedImage.length);

        Product updated = dao.updateProduct(inserted.getProductId(), changes);
        Optional<Product> fetched = dao.getProductImageById(inserted.getProductId());

        assertTrue(fetched.isPresent());

        Product actual = fetched.get();
        assertAll(
                () -> assertEquals(inserted.getProductId(), updated.getProductId()),
                () -> assertEquals(inserted.getProductId(), actual.getProductId()),
                () -> assertEquals("TEST_UpdateChanged", actual.getName()),
                () -> assertEquals(2.80, actual.getPrice()),
                () -> assertTrue(actual.isOnSale()),
                () -> assertEquals(2.10, actual.getDiscountPrice()),
                () -> assertEquals(18, actual.getStock()),
                () -> assertEquals("updated.jpeg", actual.getFileName()),
                () -> assertEquals("image/jpeg", actual.getContentType()),
                () -> assertEquals(updatedImage.length, actual.getFileSize()),
                () -> assertArrayEquals(updatedImage, actual.getProductImage())
        );
    }

    @Test
    void updateProduct_whenProductIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                dao.updateProduct(1, null)
        );

        assertEquals("product is required", exception.getMessage());
    }

    @Test
    void updateProduct_whenIdIsNotPositive_throwsIllegalArgumentException() {
        Product changes = new Product(0, "TEST_InvalidUpdate", 1.99, false, null, 4, null, null, null, 0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                dao.updateProduct(0, changes)
        );

        assertEquals("id must be positive", exception.getMessage());
    }

    @Test
    void updateProduct_whenIdDoesNotExist_throwsRuntimeException() {
        Product changes = new Product(0, "TEST_MissingUpdate", 1.99, false, null, 4, null, null, null, 0);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                dao.updateProduct(999999, changes)
        );

        assertTrue(exception.getMessage().startsWith("Failed to update product: update failed"));
    }

    @Test
    void deleteProductById_whenProductExists_removesProduct() {
        Product inserted = dao.insertProduct(new Product(0, "TEST_DeleteMilk", 1.25, false, null, 11, null, null, null, 0));

        boolean deleted = dao.deleteProductById(inserted.getProductId());

        assertAll(
                () -> assertTrue(deleted),
                () -> assertTrue(dao.getProductById(inserted.getProductId()).isEmpty())
        );
    }

    @Test
    void deleteProductById_whenIdDoesNotExist_returnsFalse() {
        assertFalse(dao.deleteProductById(999999));
    }

    @Test
    void deleteProductById_whenIdIsNotPositive_returnsFalse() {
        assertAll(
                () -> assertFalse(dao.deleteProductById(0)),
                () -> assertFalse(dao.deleteProductById(-1))
        );
    }

    @Test
    void findProductsByFilter_returnsOnlyMatchingProducts() {
        dao.insertProduct(new Product(0, "TEST_FilterLowPrice", 0.60, false, null, 30, null, null, null, 0));
        dao.insertProduct(new Product(0, "TEST_FilterHighPrice", 2.40, true, 1.95, 12, null, null, null, 0));

        List<Product> filtered = dao.findProductsByFilter(product ->
                product.getName().startsWith("TEST_") && product.getPrice() >= 2.00
        );

        assertFalse(filtered.isEmpty());
        assertTrue(filtered.stream().allMatch(product -> product.getName().startsWith("TEST_")));
        assertTrue(filtered.stream().allMatch(product -> product.getPrice() >= 2.00));
    }

    @Test
    void findProductsByFilter_whenFilterIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                dao.findProductsByFilter(null)
        );

        assertEquals("filter is required", exception.getMessage());
    }
}
