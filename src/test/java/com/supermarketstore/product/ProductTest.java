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
        product.setProductId(2);
        assertEquals(2, product.getProductId());
    }

    @Test
    void getName() {
        assertEquals("Product", product.getName());
    }

    @Test
    void setName() {
        product.setName("New Product");
        assertEquals("New Product", product.getName());
    }

    @Test
    void getPrice() {
        assertEquals(20, product.getPrice());
    }

    @Test
    void setPrice() {
        product.setPrice(30);
        assertEquals(30, product.getPrice());
    }

    @Test
    void isOnSale() {
        assertFalse(product.isOnSale());
    }

    @Test
    void setOnSale() {
        product.setOnSale(true);
        assertTrue(product.isOnSale());
    }

    @Test
    void setOnSaleDiscountPrice() {
        product.setOnSale(true);
        assertNull(product.getDiscountPrice());
    }

    @Test
    void getDiscountPrice() {
        assertNull(product.getDiscountPrice());
    }

    @Test
    void setDiscountPrice() {
        product.setOnSale(true);
        product.setDiscountPrice(15.0);
        assertEquals(15.0, product.getDiscountPrice());
    }

    @Test
    void setDiscountPriceOnSaleFalse() {
        product.setOnSale(true);
        product.setDiscountPrice(15.0);
        product.setOnSale(false);
        assertNull(product.getDiscountPrice());
    }

    @Test
    void getStock() {
        assertEquals(45, product.getStock());
    }

    @Test
    void setStock() {
        product.setStock(100);
        assertEquals(100, product.getStock());
    }

    @Test
    void testToString() {
        assertEquals("Product{productId=1, name='Product', price=20.0, onSale=false, discountPrice=null, stock=45}", product.toString());
    }
}