package com.supermarketstore.product;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Defines data access operations for Product entities.
 * Provides CRUD methods and predicate-based filtering.
 *
 * @author Nikita Smiichyk (primary)
 */
public interface ProductDao {
    List<Product> getAllProducts();

    Optional<Product> getProductById(int id);

    boolean deleteProductById(int id);

    Product insertProduct(Product product);

    Product updateProduct(int id, Product product);

    List<Product> findProductsByFilter(Predicate<Product> filter);
}
