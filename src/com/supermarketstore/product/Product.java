package com.supermarketstore.product;

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
}
