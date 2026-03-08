package com.supermarketstore.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId(1);
        product.setName("Product");
        product.setPrice(20);
        product.setOnSale(false);
        product.setStock(45);
    }

    @Test
    void getProductId() {
    }

    @Test
    void setProductId() {
    }

    @Test
    void getName() {
    }

    @Test
    void setName() {
    }

    @Test
    void getPrice() {
    }

    @Test
    void setPrice() {
    }

    @Test
    void isOnSale() {
    }

    @Test
    void setOnSale() {
    }

    @Test
    void getDiscountPrice() {
    }

    @Test
    void setDiscountPrice() {
    }

    @Test
    void getStock() {
    }

    @Test
    void setStock() {
    }

    @Test
    void testToString() {
    }
}