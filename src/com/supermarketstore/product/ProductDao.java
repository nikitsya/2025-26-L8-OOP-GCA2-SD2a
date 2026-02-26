package com.supermarketstore.product;

import java.util.List;
import java.util.Optional;

/**
 * TODO
 *
 * @author Nikita Smiichyk (primary)
 */
public interface ProductDao {
    List<Product> getAllProducts();

    Optional<Product> getProductById(int id);

    boolean deleteProductById(int id);

    Product insertProduct(Product product);

    Product updateProduct(int id, Product product);
}
