package com.supermarketstore.product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    List<Product> getAllProducts();

    Optional<Product> getProductById(int id);

    boolean deleteProductById(int id);

    // TODO insertProduct(Product product);

    // TODO updateProduct(int id, Product product);
}
