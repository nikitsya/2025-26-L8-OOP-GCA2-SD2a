package com.supermarketstore.product;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JdbcProductDaoTest {
    private static final String DB_URL = System.getProperty(
            "test.db.url",
            System.getenv().getOrDefault("TEST_DB_URL", "jdbc:mysql://localhost:3306/supermarket_store_system")
    );
    private static final String DB_USER = System.getProperty(
            "test.db.user",
            System.getenv().getOrDefault("TEST_DB_USER", "root")
    );
    private static final String DB_PASS = System.getProperty(
            "test.db.pass",
            System.getenv().getOrDefault("TEST_DB_PASS", "")
    );

    static JdbcProductDao dao;

    @BeforeAll
    static void beforeAll() {
        dao = new JdbcProductDao(
                DB_URL,
                DB_USER,
                DB_PASS);
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
