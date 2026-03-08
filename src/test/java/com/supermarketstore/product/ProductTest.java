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
    void setProductIdNegativeThrows() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setProductId(-1)
        );
        assertEquals("productId cannot be negative", ex.getMessage());
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
    void setNameEmptyThrows() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setName("")
        );
        assertEquals("Product name must not be null or blank", ex.getMessage());

        ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setName(" ")
        );
        assertEquals("Product name must not be null or blank", ex.getMessage());

        ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setName(null)
        );
        assertEquals("Product name must not be null or blank", ex.getMessage());
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
    void setPriceNegativeThrows() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setPrice(-20)
        );
        assertEquals("Product price must be greater than 0", ex.getMessage());
    }

    @Test
    void setDiscountPriceMoreThanProductPriceThrows() {
        product.setOnSale(true);
        product.setPrice(25);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setDiscountPrice(28.00)
        );
        assertEquals("Discount price must be less than product price", ex.getMessage());
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
    void setDiscountPriceProductIsNotOnSaleThrows() {
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> product.setDiscountPrice(20.30)
        );
        assertEquals("Cannot set discount price when product is not on sale", ex.getMessage());
    }

    @Test
    void setDiscountPriceNullThrows() {
        product.setOnSale(true);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setDiscountPrice(null)
        );
        assertEquals("Discount price is required when product is on sale", ex.getMessage());
    }

    @Test
    void setDiscountPriceNegativeThrows() {
        product.setOnSale(true);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setDiscountPrice(-20.30)
        );
        assertEquals("Discount price must be 0 or greater", ex.getMessage());
    }

    @Test
    void setDiscountPrice0Throws() {
        product =  new Product();
        product.setOnSale(true);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> product.setDiscountPrice(23.00)
        );
        assertEquals("Price must be set before discount price", ex.getMessage());
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
    void setStockNegativeThrows() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setStock(-1)
        );
        assertEquals("Stock cannot be negative", ex.getMessage());
    }

    @Test
    void testToString() {
        assertEquals("Product{productId=1, name='Product', price=20.0, onSale=false, discountPrice=null, stock=45}", product.toString());
    }
}