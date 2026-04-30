package com.supermarketstore.product;

import java.util.List;

/**
 * Defines JSON conversion operations for Product entities.
 * Provides conversion for a single Product and for Product lists.
 *
 * @author Nikita Smiichyk (primary)
 */
public interface ProductJsonConverter {

    /**
     * Serialises a product to JSON.
     *
     * @param entity the product to serialise
     * @return the JSON representation of the product
     * @throws IllegalArgumentException if the product is null or cannot be serialised
     */
    String productToJson(Product entity);

    /**
     * Deserialises JSON into a product.
     *
     * @param json the JSON representation of a product
     * @return the product represented by the JSON input
     * @throws IllegalArgumentException if the JSON input is blank or cannot be deserialised
     */
    Product productFromJson(String json);

    /**
     * Serialises a list of products to JSON.
     *
     * @param list the products to serialise
     * @return the JSON representation of the product list
     * @throws IllegalArgumentException if the list is null or cannot be serialised
     */
    String productListToJson(List<Product> list);

    /**
     * Deserialises JSON into a list of products.
     *
     * @param json the JSON representation of a product list
     * @return the products represented by the JSON input
     * @throws IllegalArgumentException if the JSON input is blank or cannot be deserialised
     */
    List<Product> productListFromJson(String json);
}
