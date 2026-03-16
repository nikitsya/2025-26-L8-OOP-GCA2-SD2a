package com.supermarketstore.product;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JacksonProductJsonConverterTest {

    JacksonProductJsonConverter converter = new JacksonProductJsonConverter();
    Product  product = new Product(1, "cucumber", 0.65, false, null, 98);
    String jsonString = "{\"product_id\":1,\"name\":\"cucumber\",\"price\":0.65,\"is_on_sale\":false,\"discount_price\":null,\"stock\":98}";
    List<Product> productList = new ArrayList<>();

    @Test
    void productToJson() {
        assertEquals(converter.productToJson(product), jsonString);
    }

    @Test
    void productFromJson() {
        assertEquals(converter.productFromJson(jsonString), product);
    }

    @Test
    void productListToJson() {
        productList.add(product);
        productList.add(product);
        String expected = "[" + jsonString + "," + jsonString + "]";
        assertEquals(converter.productListToJson(productList), expected);
    }
}