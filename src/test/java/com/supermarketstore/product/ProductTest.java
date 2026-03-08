package com.supermarketstore.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1, "Product", 20, false, null, 45);
    }

    @Test
    void getProductId_returnsProductId() {
        assertEquals(1, product.getProductId());
    }

    @Test
    void setProductId_withValidValue_updatesProductId() {
        product.setProductId(2);
        assertEquals(2, product.getProductId());
    }

    @Test
    void setProductId_withNegativeValue_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setProductId(-1)
        );
        assertEquals("productId cannot be negative", ex.getMessage());
    }

    @Test
    void getName_returnsName() {
        assertEquals("Product", product.getName());
    }

    @Test
    void setName_withValidValue_updatesName() {
        product.setName("New Product");
        assertEquals("New Product", product.getName());
    }

    @Test
    void setName_withValidValueAndWhitespace_trimsAndUpdatesName() {
        product.setName("   New Product ");
        assertEquals("New Product", product.getName());
    }

    @Test
    void setName_withBlankOrNullValue_throwsIllegalArgumentException() {
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
    void getPrice_returnsPrice() {
        assertEquals(20, product.getPrice());
    }

    @Test
    void setPrice_withValidValue_updatesPrice() {
        product.setPrice(30);
        assertEquals(30, product.getPrice());
    }

    @Test
    void setPrice_whenOnSaleAndDiscountIsLessThanNewPrice_updatesPrice() {
        product.setOnSale(true);
        product.setDiscountPrice(10.0);

        product.setPrice(20.0);

        assertEquals(20.0, product.getPrice());
    }

    @Test
    void setPrice_withNegativeValue_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setPrice(-20)
        );
        assertEquals("Product price must be greater than 0", ex.getMessage());
    }

    @Test
    void setPrice_whenLessThanDiscountPrice_throwsIllegalArgumentException() {
        product.setOnSale(true);
        product.setPrice(30);
        product.setDiscountPrice(28.00);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setPrice(25)
        );
        assertEquals("Discount price must be less than product price", ex.getMessage());
    }

    @Test
    void isOnSale_returnsFalseFromSetup() {
        assertFalse(product.isOnSale());
    }

    @Test
    void setOnSale_withTrue_updatesSaleStatus() {
        product.setOnSale(true);
        assertTrue(product.isOnSale());
    }

    @Test
    void setOnSale_whenTrue_keepsDiscountPriceNull() {
        product.setOnSale(true);
        assertNull(product.getDiscountPrice());
    }

    @Test
    void getDiscountPrice_returnsNullFromSetup() {
        assertNull(product.getDiscountPrice());
    }

    @Test
    void setDiscountPrice_withValidValue_updatesDiscountPrice() {
        product.setOnSale(true);
        product.setDiscountPrice(15.0);
        assertEquals(15.0, product.getDiscountPrice());
    }

    @Test
    void setDiscountPrice_whenProductIsNotOnSale_throwsIllegalStateException() {
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> product.setDiscountPrice(20.30)
        );
        assertEquals("Cannot set discount price when product is not on sale", ex.getMessage());
    }

    @Test
    void setDiscountPrice_whenNullAndOnSale_throwsIllegalArgumentException() {
        product.setOnSale(true);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setDiscountPrice(null)
        );
        assertEquals("Discount price is required when product is on sale", ex.getMessage());
    }

    @Test
    void setDiscountPrice_withNegativeValue_throwsIllegalArgumentException() {
        product.setOnSale(true);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setDiscountPrice(-20.30)
        );
        assertEquals("Discount price must be 0 or greater", ex.getMessage());
    }

    @Test
    void setDiscountPrice_whenPriceNotSet_throwsIllegalStateException() {
        product = new Product();
        product.setOnSale(true);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> product.setDiscountPrice(23.00)
        );
        assertEquals("Price must be set before discount price", ex.getMessage());
    }

    @Test
    void setDiscountPrice_whenGreaterThanPrice_throwsIllegalArgumentException() {
        product.setOnSale(true);
        product.setPrice(25);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setDiscountPrice(28.00)
        );
        assertEquals("Discount price must be less than product price", ex.getMessage());
    }

    @Test
    void constructor_whenProductIsNotOnSaleAndDiscountPriceProvided_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Product(1, "Product", 20.0, false, 15.0, 45)
        );
        assertEquals("Discount price must be null when product is not on sale", ex.getMessage());
    }

    @Test
    void constructor_whenProductIsOnSaleAndDiscountPriceProvided_setsDiscountPrice() {
        Product saleProduct = new Product(2, "Sale Product", 30.0, true, 20.0, 10);

        assertAll(
                () -> assertTrue(saleProduct.isOnSale()),
                () -> assertEquals(20.0, saleProduct.getDiscountPrice()),
                () -> assertEquals(30.0, saleProduct.getPrice())
        );
    }

    @Test
    void setOnSale_whenFalse_clearsDiscountPrice() {
        product.setOnSale(true);
        product.setDiscountPrice(15.0);
        product.setOnSale(false);
        assertNull(product.getDiscountPrice());
    }

    @Test
    void getStock_returnsStock() {
        assertEquals(45, product.getStock());
    }

    @Test
    void setStock_withValidValue_updatesStock() {
        product.setStock(100);
        assertEquals(100, product.getStock());
    }

    @Test
    void setStock_withNegativeValue_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> product.setStock(-1)
        );
        assertEquals("Stock cannot be negative", ex.getMessage());
    }

    @Test
    void toString_returnsFormattedProduct() {
        assertEquals("Product{productId=1, name='Product', price=20.0, onSale=false, discountPrice=null, stock=45}", product.toString());
    }
}
