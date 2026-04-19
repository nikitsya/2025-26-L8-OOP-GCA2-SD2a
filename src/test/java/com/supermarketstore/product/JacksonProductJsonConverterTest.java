package com.supermarketstore.product;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JacksonProductJsonConverter}.
 *
 * @author Nikita Smechik
 */
class JacksonProductJsonConverterTest {

    JacksonProductJsonConverter converter = new JacksonProductJsonConverter();

    Product product = new Product(1, "cucumber", 0.65, false, null, 98, null, null, null, 0);
    List<Product> products = List.of(product, product);
    String product_json = "{\"product_id\":1,\"name\":\"cucumber\",\"price\":0.65,\"is_on_sale\":false,\"discount_price\":null,\"stock\":98}";
    String products_json = "[" + product_json + "," + product_json + "]";

    @Test
    void productToJson() {
        assertEquals(product_json, converter.productToJson(product));
    }

    @Test
    void productToJson_whenProductIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> converter.productToJson(null)
        );
        assertEquals("Product must not be null", ex.getMessage());
    }

    @Test
    void productFromJson() {
        assertEquals(product, converter.productFromJson(product_json));
    }

    @Test
    void productFromJson_whenJsonIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> converter.productFromJson(null)
        );
        assertEquals("Product JSON must not be null or blank", ex.getMessage());
    }

    @Test
    void productFromJson_whenJsonIsBlank_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> converter.productFromJson("  ")
        );
        assertEquals("Product JSON must not be null or blank", ex.getMessage());
    }

    @Test
    void productFromJson_whenJsonIsMalformed_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> converter.productFromJson("{bad json}")
        );
        assertEquals("Failed to deserialize Product from JSON: ", ex.getMessage());
    }

    @Test
    void productListToJson() {
        assertEquals(products_json, converter.productListToJson(products));
    }

    @Test
    void productListToJson_whenListIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> converter.productListToJson(null)
        );
        assertEquals("Product list must not be null", ex.getMessage());
    }

    @Test
    void productListFromJson() {
        assertEquals(products, converter.productListFromJson(products_json));
    }

    @Test
    void productListFromJson_whenJsonIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> converter.productListFromJson(null)
        );
        assertEquals("Product JSON must not be null or blank", ex.getMessage());
    }

    @Test
    void productListFromJson_whenJsonIsBlank_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> converter.productListFromJson("  ")
        );
        assertEquals("Product JSON must not be null or blank", ex.getMessage());
    }

    @Test
    void productListFromJson_whenJsonIsEmptyArray_returnsEmptyList() {
        List<Product> result = converter.productListFromJson("[]");
        assertTrue(result.isEmpty());
    }

    @Test
    void productListFromJson_whenJsonIsMalformed_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> converter.productListFromJson("[{bad json}]")
        );
        assertEquals("Failed to deserialize Product list from JSON", ex.getMessage());
    }
}
