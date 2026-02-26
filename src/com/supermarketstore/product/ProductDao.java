package com.supermarketstore.product;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ProductDao {
    List<Product> getAllProducts();
    Optional<Product> getProductById(int id);
    boolean deleteProductById(int id);
    // TODO insertProduct(Product product);
    // TODO updateProduct(int id, Product product);
    List<Product> findProductsByFilter(Predicate<Product> filter);
    String productToJson(Product entity);
    Product productFromJson(String json);
    String productListToJson(List<Product> list);
}
