package com.supermarketstore.product;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * TODO
 *
 * @author Nikita Smiichyk (primary)
 */
public class JdbcProductDao implements ProductDao, ProductJsonConverter {

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public Optional<Product> getProductById(int id) {
        return Optional.empty();
    }

    @Override
    public boolean deleteProductById(int id) {
        return false;
    }

    @Override
    public Product insertProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProduct(int id, Product product) {
        return null;
    }

    @Override
    public List<Product> findProductsByFilter(Predicate<Product> filter) {
        return List.of();
    }

    @Override
    public String productToJson(Product entity) {
        return "";
    }

    @Override
    public Product productFromJson(String json) {
        return null;
    }

    @Override
    public String productListToJson(List<Product> list) {
        return "";
    }
}
