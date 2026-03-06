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
        if (id <= 0) return Optional.empty();

        String sql = "SELECT product_id, name, price, is_on_sale, discount_price, stock, department_id FROM products WHERE product_id = ?";

        try (Connection connection = open();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private Product mapRow(ResultSet resultSet) throws SQLException {
        int productId = resultSet.getInt("product_id");;
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        boolean onSale =  resultSet.getBoolean("onSale");
        Double discountPrice = resultSet.getDouble("discountPrice");
        int stock = resultSet.getInt("stock");
        int departmentId = resultSet.getInt("department_id");

        return new Product(productId, name, price, onSale, discountPrice, stock, departmentId);
    }
}
