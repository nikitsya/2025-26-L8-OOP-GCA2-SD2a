package com.supermarketstore.product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * JDBC implementation of ProductDao for MySQL.
 * Provides data access operations for Product entities:
 * retrieving all products, retrieving a product by ID, and deleting by ID.
 * Maps each SQL ResultSet row to a Product domain object.
 *
 * @author Nikita Smiichyk (primary)
 */
public record JdbcProductDao(String _url, String _user, String _pass) implements ProductDao {

    public JdbcProductDao(String _url, String _user, String _pass) {
        if (_url == null || _url.isBlank()) throw new IllegalArgumentException("url is required");
        this._url = _url.trim();
        this._user = _user;
        this._pass = _pass;
    }

    @Override
    public List<Product> getAllProducts() {
        String sql = "SELECT product_id, name, price, is_on_sale, discount_price, stock FROM products";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            ArrayList<Product> out = new ArrayList<>();
            while (rs.next()) out.add(mapRow(rs));
            return out;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> getProductById(int id) {
        if (id <= 0) return Optional.empty();

        String sql = "SELECT product_id, name, price, is_on_sale, discount_price, stock FROM products WHERE product_id = ?";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteProductById(int id) {
        if (id <= 0) return false;

        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product insertProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("product is required");

        String sql = "INSERT INTO products (name, price, is_on_sale, discount_price, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setBoolean(3, product.isOnSale());
            ps.setDouble(4, product.getDiscountPrice());
            ps.setInt(5, product.getStock());

            int rows = ps.executeUpdate();
            if (rows != 1) throw new IllegalStateException("insert failed, rows=" + rows);

            return new Product();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert product", e);
        }
    }

    @Override
    public Product updateProduct(int id, Product product) {
        if (product == null) throw new IllegalArgumentException("");

        String sql = "INSERT INTO products (name, price, is_on_sale, discount_price, stock) VALUES (?, ?, ?, ?, ?) WHERE product_id = ?";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            // TODO
            return new Product();
        } catch (SQLException e) {
            throw new RuntimeException("", e);
        }
    }

    @Override
    public List<Product> findProductsByFilter(Predicate<Product> filter) {
        // TODO
        return List.of();
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(_url, _user, _pass);
    }

    private Product mapRow(ResultSet resultSet) throws SQLException {
        int productId = resultSet.getInt("product_id");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        boolean onSale = resultSet.getBoolean("is_on_sale");
        Double discountPrice = resultSet.getDouble("discount_price");
        int stock = resultSet.getInt("stock");

        return new Product(productId, name, price, onSale, discountPrice, stock);
    }
}
