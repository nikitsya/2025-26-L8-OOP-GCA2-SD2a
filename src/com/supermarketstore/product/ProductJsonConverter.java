package com.supermarketstore.product;

import java.util.List;

/**
 * TODO
 *
 * @author Nikita Smiichyk (primary)
 */
public interface ProductJsonConverter {
    String productToJson(Product entity);

    Product productFromJson(String json);

    String productListToJson(List<Product> list);
}
