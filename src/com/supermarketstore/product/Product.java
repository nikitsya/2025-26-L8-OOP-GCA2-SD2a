package com.supermarketstore.product;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private String name;
    private BigDecimal price;
    private boolean onSale;
    private BigDecimal discountPrice;
    private int stock;
    private int departmentId;

    public Product(String name, BigDecimal price, boolean onSale, BigDecimal discountPrice, int stock, int departmentId) {
        this.name = name;
        this.price = price;
        this.onSale = onSale;
        this.discountPrice = discountPrice;
        this.stock = stock;
        this.departmentId = departmentId;
    }
}
