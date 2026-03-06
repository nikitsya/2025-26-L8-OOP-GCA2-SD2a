package com.supermarketstore.product;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * TODO
 *
 * @author Nikita Smiichyk (primary)
 */
public record JdbcProductDao(String _url, String _user, String _pass) implements ProductDao, ProductJsonConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public JdbcProductDao(String _url, String _user, String _pass) {
        if (_url == null || _url.isBlank()) throw new IllegalArgumentException("url is required");
        this._url = _url.trim();
        this._user = _user;
        this._pass = _pass;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(_url, _user, _pass);
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

        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private Product mapRow(ResultSet resultSet) throws SQLException {
        int productId = resultSet.getInt("product_id");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        boolean onSale = resultSet.getBoolean("is_on_sale");
        Double discountPrice = resultSet.getDouble("discount_price");
        int stock = resultSet.getInt("stock");

        return new Product(productId, name, price, onSale, discountPrice, stock);
    }

    @Override
    public String productToJson(Product entity) {
        return "";
    }

    @Override
    public Product productFromJson(String json) {
        if (json == null || json.isBlank()) throw new IllegalArgumentException("json is required");
        try {
            return MAPPER.readValue(json, Product.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid product JSON", e);
        }
    }

    @Override
    public String productListToJson(List<Product> list) {
        try {
            return MAPPER.writeValueAsString(list);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid product JSON", e);
        }
    }
}
