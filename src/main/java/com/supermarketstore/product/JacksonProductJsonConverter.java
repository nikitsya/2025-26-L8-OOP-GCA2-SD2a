package com.supermarketstore.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Converts Product objects to/from JSON using Jackson.
 *
 * @author Nikita Smiichyk (primary)
 */
public class JacksonProductJsonConverter implements ProductJsonConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String productToJson(Product product) {
        if (product == null) throw new IllegalArgumentException("Product must not be null");
        try {
            return MAPPER.writeValueAsString(product);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to serialize Product to JSON", e);
        }
    }

    @Override
    public Product productFromJson(String json) {
        if (json == null || json.isBlank())
            throw new IllegalArgumentException("Product JSON must not be null or blank");
        try {
            return MAPPER.readValue(json, Product.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to deserialize Product from JSON: ", e);
        }
    }

    @Override
    public String productListToJson(List<Product> products) {
        if (products == null) throw new IllegalArgumentException("Product list must not be null");
        try {
            return MAPPER.writeValueAsString(products);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to serialize Product list to JSON", e);
        }
    }

    @Override
    public List<Product> productListFromJson(String json) {
        if (json == null || json.isBlank())
            throw new IllegalArgumentException("Product JSON must not be null or blank");
        try {
            return MAPPER.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to deserialize Product list from JSON", e);
        }
    }
}
