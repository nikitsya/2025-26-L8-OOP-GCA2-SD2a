package com.supermarketstore.product;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;


/**
 * Converts Product objects to/from JSON using Jackson.
 *
 * @author Nikita Smiichyk (primary)
 */
public class JacksonProductJsonConverter implements  ProductJsonConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String productToJson(Product entity) {
        if (entity == null) throw new IllegalArgumentException("Product must not be null");
        try {
            return MAPPER.writeValueAsString(entity);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to serialize Product to JSON", e);
        }
    }

    @Override
    public Product productFromJson(String json) {
        if (json == null || json.isBlank()) throw new IllegalArgumentException("Product JSON must not be null or blank");
        try {
            return MAPPER.readValue(json, Product.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to deserialize Product from JSON: ", e);
        }
    }

    @Override
    public String productListToJson(List<Product> list) {
        if (list == null) throw new IllegalArgumentException("Product list must not be null");
        try {
            return MAPPER.writeValueAsString(list);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to serialize Product list to JSON", e);
        }
    }
}
