package com.supermarketstore.product;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class JdbcProductDaoTest {
    private static final String DB_URL = System.getProperty("test.db.url", System.getenv().getOrDefault("TEST_DB_URL", "jdbc:mysql://localhost:3306/supermarket_store_system"));
    private static final String DB_USER = System.getProperty("test.db.user", System.getenv().getOrDefault("TEST_DB_USER", "root"));
    private static final String DB_PASS = System.getProperty("test.db.pass", System.getenv("TEST_DB_PASS"));

    static JdbcProductDao dao;

    @BeforeAll
    static void beforeAll() {
        if (DB_PASS == null || DB_PASS.isBlank()) fail("Set TEST_DB_PASS or -Dtest.db.pass for JdbcProductDaoTest");

        dao = new JdbcProductDao(DB_URL, DB_USER, DB_PASS);

        // add test products to the database
        Product product1 = new Product(30, "TEST_cucumber", 0.65, false, null, 98);
        Product product2 = new Product(31, "TEST_cucumber", 0.70, true, 0.65, 126);
        dao.insertProduct(product1);
        dao.insertProduct(product2);
    }

    @AfterAll
    static void afterAll() {
        dao = null;
    }

    @Test
    void getAllProducts() {
    }

    @Test
    void getProductById() {
    }

    @Test
    void deleteProductById() {
    }

    @Test
    void insertProduct() {
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
