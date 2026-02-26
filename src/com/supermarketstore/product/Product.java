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
        this.name = name;
        this.price = price;
        this.onSale = onSale;
        this.discountPrice = discountPrice;
        this.stock = stock;
        this.departmentId = departmentId;
    }

    public int getProductId() {
        return productId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public boolean isOnSale() {
        return onSale;
    }
    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }
    public Double getDiscountPrice() {
        return discountPrice;
    }
    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public int getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
