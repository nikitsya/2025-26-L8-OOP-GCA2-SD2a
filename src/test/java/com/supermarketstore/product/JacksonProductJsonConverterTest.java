package com.supermarketstore.product;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JacksonProductJsonConverterTest {

    JacksonProductJsonConverter converter = new JacksonProductJsonConverter();
    Product product = new Product(1, "cucumber", 0.65, false, null, 98);
    String jsonString = "{\"product_id\":1,\"name\":\"cucumber\",\"price\":0.65,\"is_on_sale\":false,\"discount_price\":null,\"stock\":98}";

    @Test
    void productToJson() {
        assertEquals(jsonString, converter.productToJson(product));
    }

    @Test
    void productFromJson() {
        assertEquals(product, converter.productFromJson(jsonString));
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
    void productListToJson() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product);
        String expected = "[" + jsonString + "," + jsonString + "]";
        assertEquals(expected, converter.productListToJson(productList));
    }

    @Test
    void productListToJson_whenListIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> converter.productListToJson(null)
        );
        assertEquals("Product list must not be null", ex.getMessage());
    }
}
