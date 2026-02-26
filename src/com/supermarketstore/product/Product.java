package com.supermarketstore.product;

/** Represents a product entity in the supermarket system. */
public class Product {
    private int productId;
    private String name;
    private double price;
    private boolean onSale;
    private Double discountPrice;
    private int stock;
    private int departmentId;

    public Product(String name, double price, boolean onSale, Double discountPrice, int stock, int departmentId) {
        setName(name);
        setPrice(price);
        this.onSale = onSale;
        setDiscountPrice(discountPrice);
        setStock(stock);
        this.departmentId = departmentId;
    }

    public int getProductId() {
        return productId;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public boolean isOnSale() {
        return onSale;
    }
    public Double getDiscountPrice() {
        return discountPrice;
    }
    public int getStock() {
        return stock;
    }
    public int getDepartmentId() {
        return departmentId;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        this.name = name;
    }
    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Product price cannot be less than 0");
        }
        this.price = price;
    }
    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }
    public void setDiscountPrice(Double discountPrice) {
        if (discountPrice != null && discountPrice < 0) {
            throw new IllegalArgumentException("Discount price cannot be negative");
        }
        this.discountPrice = discountPrice;
    }
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Product stock cannot be less than 0");
        }
        this.stock = stock;
    }
    public void setDepartmentId(int departmentId) {
        if (departmentId <= 0) {
            throw new IllegalArgumentException("Department id cannot be less than 0");
        }
        this.departmentId = departmentId;
    }
}
