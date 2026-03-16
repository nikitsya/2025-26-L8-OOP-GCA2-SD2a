package com.supermarketstore.product;

import java.util.List;

/**
 * Defines JSON conversion operations for Product entities.
 * Provides conversion for a single Product and for Product lists.
 *
 * @author Nikita Smiichyk (primary)
 */
public interface ProductJsonConverter {

    String productToJson(Product entity);

    Product productFromJson(String json);

    String productListToJson(List<Product> list);

    List<Product> productListFromJson(String json);
}
