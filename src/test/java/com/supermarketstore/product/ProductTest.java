package com.supermarketstore.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void shouldTrimProductName() {
        Product product = new Product(1, "Milk", 2.5, false, null, 10, 1);
        assertEquals("Milk", product.getName());
    }

    @Test
    void shouldRejectNegativeStock() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Product(1, "Bread", 1.2, false, null, -1, 2)
        );
        assertEquals("Stock cannot be negative", exception.getMessage());
    }
}
