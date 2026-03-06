package com.supermarketstore.product;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * TODO
 *
 * @author Nikita Smiichyk (primary)
 */
public class JdbcProductDao implements ProductDao, ProductJsonConverter {

    private String _url;
    private String _user;
    private String _pass;

    public JdbcProductDao(String url, String user, String pass) {
        if (url == null || url.isBlank()) throw new IllegalArgumentException("url is required");
        _url = url.trim();
        _user = user;
        _pass = pass;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(_url, _user, _pass);
    }

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
