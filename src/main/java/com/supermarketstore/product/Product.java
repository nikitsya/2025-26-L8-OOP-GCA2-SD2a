package com.supermarketstore.product;

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
    private int departmentId;

    public Product(int productId, String name, double price, boolean onSale, Double discountPrice, int stock, int departmentId) {
        setProductId(productId);
        setName(name);
        setPrice(price);
        setOnSale(onSale);
        setDiscountPrice(discountPrice);
        setStock(stock);
        setDepartmentId(departmentId);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        if(productId < 0) {
            throw new IllegalArgumentException("productId cannot be negative");
        }
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name must not be null or blank");
        }
        this.name = name.trim();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("Product price must be greater than 0");
        if (onSale && discountPrice != null && discountPrice >= price) {
            throw new IllegalArgumentException("Discount price must be less than product price");
        }
        this.price = price;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
        if (!onSale) discountPrice = null;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        if (!onSale) {
            this.discountPrice = null;
        } else {
            if (discountPrice == null)
                throw new IllegalArgumentException("Discount price is required when product is on sale");
            if (discountPrice < 0) throw new IllegalArgumentException("Discount price must be 0 or greater");
            if (discountPrice >= price)
                throw new IllegalArgumentException("Discount price must be less than product price");
            this.discountPrice = discountPrice;
        }
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");
        this.stock = stock;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        if (departmentId <= 0) throw new IllegalArgumentException("Department id must be greater than 0");
        this.departmentId = departmentId;
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
                ", departmentId=" + departmentId +
                '}';
    }
}
