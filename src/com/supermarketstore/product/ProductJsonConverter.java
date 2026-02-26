package com.supermarketstore.product;

import java.util.List;
import java.util.function.Predicate;

public interface ProductJsonConverter {
    List<Product> findProductsByFilter(Predicate<Product> filter);

    String productToJson(Product entity);

    Product productFromJson(String json);

    String productListToJson(List<Product> list);
}
