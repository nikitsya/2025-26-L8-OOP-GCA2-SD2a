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
        String sql = "SELECT product_id, name, price, is_on_sale, discount_price, stock, file_data, file_name, " +
                "content_type, file_size FROM supermarket_store_system.products";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            ArrayList<Product> out = new ArrayList<>();
            while (rs.next()) out.add(mapRow(rs));
            return out;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Product> getProductById(int id) {
        if (id <= 0) return Optional.empty();

        String sql = "SELECT product_id, name, price, is_on_sale, discount_price, stock, file_data, file_name, " +
                "content_type, file_size FROM supermarket_store_system.products WHERE product_id = ?";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteProductById(int id) {
        if (id <= 0) return false;

        String sql = "DELETE FROM supermarket_store_system.products WHERE product_id = ?";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Product insertProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("product is required");

        String sql = "INSERT INTO supermarket_store_system.products(name, price, is_on_sale, discount_price, stock, " +
                "file_data, file_name, content_type, file_size) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindProductParams(ps, product);

            int rows = ps.executeUpdate();
            if (rows != 1) throw new IllegalStateException("insert failed, rows=" + rows);

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) throw new IllegalStateException("no generated key returned");
                product.setProductId(keys.getInt(1));
            }
        } catch (SQLException | IllegalStateException e) {
            throw new RuntimeException("Failed to insert product: " + e.getMessage(), e);
        }

        return product;
    }

    @Override
    public Product updateProduct(int id, Product product) {
        if (product == null) throw new IllegalArgumentException("product is required");
        if (id <= 0) throw new IllegalArgumentException("id must be positive");

        String sql = "UPDATE supermarket_store_system.products SET name = ?, price = ?, is_on_sale = ?, discount_price = ?, " +
                "stock = ?, file_data = ?, file_name = ?, content_type = ?, file_size = ? WHERE product_id = ?";

        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            bindProductParams(ps, product);
            ps.setInt(10, id);
            int rows = ps.executeUpdate();
            if (rows != 1) throw new IllegalStateException("update failed, rows=" + rows);
            product.setProductId(id);
        } catch (SQLException | IllegalStateException e) {
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }

        return product;
    }

    @Override
    public List<Product> findProductsByFilter(Predicate<Product> filter) {
        if (filter == null) throw new IllegalArgumentException("filter is required");
        return getAllProducts().stream().filter(filter).toList();
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(_url, _user, _pass);
    }

    private Product mapRow(ResultSet resultSet) throws SQLException {
        int productId = resultSet.getInt("product_id");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        boolean onSale = resultSet.getBoolean("is_on_sale");
        Double discountPrice = resultSet.getObject("discount_price", Double.class);
        int stock = resultSet.getInt("stock");
        byte[] fileData = resultSet.getBytes("file_data");
        String fileName = resultSet.getString("file_name");
        String contentType = resultSet.getString("content_type");
        int fileSize = resultSet.getInt("file_size");

        return new Product(productId, name, price, onSale, discountPrice, stock, fileData, fileName, contentType, fileSize);
    }

    private void bindProductParams(PreparedStatement ps, Product product) throws SQLException {
        ps.setString(1, product.getName());
        ps.setDouble(2, product.getPrice());
        ps.setBoolean(3, product.isOnSale());

        if (product.getDiscountPrice() == null) ps.setNull(4, Types.DOUBLE);
        else ps.setDouble(4, product.getDiscountPrice());

        ps.setInt(5, product.getStock());

        if (product.getFileData() == null) ps.setNull(6, Types.BINARY);
        else ps.setBytes(6, product.getFileData());

        if (product.getFileName() == null) ps.setNull(7, Types.VARCHAR);
        else ps.setString(7, product.getFileName());

        if (product.getContentType() == null) ps.setNull(8, Types.VARCHAR);
        else ps.setString(8, product.getContentType());

        ps.setInt(9, product.getFileSize());
    }
}
