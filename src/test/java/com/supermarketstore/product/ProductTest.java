package com.supermarketstore.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1);
        product.setName("Product");
        product.setPrice(20);
        product.setOnSale(false);
        product.setStock(45);
    }

    @Test
    void getProductId() {
        assertEquals(1, product.getProductId());
    }

    @Test
    void setProductId() {
    }

    @Test
    void getName() {
        assertEquals("Product", product.getName());
    }

    @Test
    void setName() {
    }

    @Test
    void getPrice() {
        assertEquals(20, product.getPrice());
    }

    @Test
    void setPrice() {
    }

    @Test
    void isOnSale() {
        assertFalse(product.isOnSale());
    }

    @Test
    void setOnSale() {
    }

    @Test
    void getDiscountPrice() {
        assertNull(product.getDiscountPrice());
    }

    @Test
    void setDiscountPrice() {
    }

    @Test
    void getStock() {
        assertEquals(45, product.getStock());
    }

    @Test
    void setStock() {
    }

    @Test
    void testToString() {
    }
}