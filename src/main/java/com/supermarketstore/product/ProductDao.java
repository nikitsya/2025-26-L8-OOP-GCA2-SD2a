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

    /**
     * Retrieves all products without loading image bytes.
     *
     * @return all products stored in the data source
     */
    List<Product> getAllProducts();

    /**
     * Retrieves one product without loading image bytes.
     *
     * @param id the product identifier
     * @return the matching product, or an empty optional when no product exists
     */
    Optional<Product> getProductById(int id);

    /**
     * Retrieves one product including its image bytes.
     *
     * @param id the product identifier
     * @return the matching product with image data, or an empty optional when no product exists
     */
    Optional<Product> getProductImageById(int id);

    /**
     * Deletes one product.
     *
     * @param id the product identifier
     * @return true when a product was deleted, otherwise false
     */
    boolean deleteProductById(int id);

    /**
     * Inserts a new product and returns the stored entity with its generated identifier.
     *
     * @param product the product to insert
     * @return the inserted product with its generated identifier
     * @throws IllegalArgumentException if the product is null
     */
    Product insertProduct(Product product);

    /**
     * Updates an existing product.
     *
     * @param id      the product identifier
     * @param product the replacement product values
     * @return the updated product
     * @throws IllegalArgumentException if the id or product is invalid
     */
    Product updateProduct(int id, Product product);

    /**
     * Filters products in memory using the supplied predicate.
     *
     * @param filter the predicate used to select products
     * @return products that match the predicate
     * @throws IllegalArgumentException if the filter is null
     */
    List<Product> findProductsByFilter(Predicate<Product> filter);
}
