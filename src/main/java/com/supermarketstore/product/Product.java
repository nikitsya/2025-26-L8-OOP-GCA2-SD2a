package com.supermarketstore.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Represents a product entity in the supermarket system.
 *
 * @author Nikita Smiichyk (primary)
 */
public class Product {
    private int productId;
    private String name;
    private double price;
    private boolean onSale;
    private Double discountPrice;
    private int stock;
    private byte[] fileData;
    private String fileName;
    private String contentType;
    private int fileSize;

    public Product() {
    }

    public Product(int productId, String name, double price, boolean onSale, Double discountPrice, int stock,
                   byte[] fileData, String fileName, String contentType, int fileSize) {
        setProductId(productId);
        setName(name);
        setPrice(price);
        setStock(stock);
        setOnSale(onSale);
        if (onSale) setDiscountPrice(discountPrice);
        else if (discountPrice != null) {
            throw new IllegalArgumentException("Discount price must be null when product is not on sale");
        }
        setFileData(fileData);
        setFileName(fileName);
        setContentType(contentType);
        setFileSize(fileSize);
    }

    @JsonProperty("product_id")
    public int getProductId() {
        return productId;
    }

    @JsonProperty("product_id")
    public void setProductId(int productId) {
        if (productId < 0) {
            throw new IllegalArgumentException("productId cannot be negative");
        }
        this.productId = productId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name must not be null or blank");
        }
        this.name = name.trim();
    }

    @JsonProperty("price")
    public double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("Product price must be greater than 0");
        if (onSale && discountPrice != null && discountPrice >= price) {
            throw new IllegalArgumentException("Discount price must be less than product price");
        }
        this.price = price;
    }

    @JsonProperty("is_on_sale")
    public boolean isOnSale() {
        return onSale;
    }

    @JsonProperty("is_on_sale")
    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
        if (!onSale) discountPrice = null;
    }

    @JsonProperty("discount_price")
    public Double getDiscountPrice() {
        return discountPrice;
    }

    @JsonProperty("discount_price")
    public void setDiscountPrice(Double discountPrice) {
        if (!onSale && discountPrice != null)
            throw new IllegalStateException("Cannot set discount price when product is not on sale");
        if (onSale && discountPrice == null)
            throw new IllegalArgumentException("Discount price is required when product is on sale");
        if (discountPrice != null) {
            if (discountPrice < 0)
                throw new IllegalArgumentException("Discount price must be 0 or greater");
            if (price <= 0)
                throw new IllegalStateException("Price must be set before discount price");
            if (discountPrice >= price)
                throw new IllegalArgumentException("Discount price must be less than product price");
        }
        this.discountPrice = discountPrice;
    }

    @JsonProperty("stock")
    public int getStock() {
        return stock;
    }

    @JsonProperty("stock")
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");
        this.stock = stock;
    }

    @JsonProperty("file_data")
    public  byte[] getFileData() {
        return fileData;
    }

    @JsonProperty("file_data")
    public  void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    @JsonProperty("file_name")
    public  String getFileName() {
        return fileName;
    }

    @JsonProperty("file_name")
    public  void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("content_type")
    public  String getContentType() {
        return contentType;
    }

    @JsonProperty("content_type")
    public  void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty("file_size")
    public  int getFileSize() {
        return fileSize;
    }

    @JsonProperty("file_size")
    public  void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", onSale=" + onSale +
                ", discountPrice=" + discountPrice +
                ", stock=" + stock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId == product.productId && Double.compare(price, product.price) == 0 && onSale == product.onSale && stock == product.stock && Objects.equals(name, product.name) && Objects.equals(discountPrice, product.discountPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, price, onSale, discountPrice, stock);
    }
}
