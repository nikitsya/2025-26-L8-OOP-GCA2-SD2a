-- Reset and recreate the development database from scratch.
DROP DATABASE IF EXISTS supermarket_store_system;
CREATE DATABASE supermarket_store_system;
USE supermarket_store_system;

-- Store departments available in the supermarket.
CREATE TABLE departments
(
    department_id    INT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    floor            INT          NOT NULL,
    zone             INT          NOT NULL,
    budget           DOUBLE       NOT NULL,
    employee_count   INT          NOT NULL,
    is_refrigerated  BOOLEAN      NOT NULL DEFAULT FALSE,
    file_name        VARCHAR(255) NOT NULL DEFAULT '',
    content_type     VARCHAR(100) NOT NULL DEFAULT '',
    file_size        INT          NOT NULL DEFAULT 0,
    department_image MEDIUMBLOB
);

-- Store products that can be sold in the supermarket.
CREATE TABLE products
(
    product_id     INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    price          DOUBLE       NOT NULL,
    is_on_sale     BOOLEAN      NOT NULL DEFAULT FALSE,
    discount_price DOUBLE,
    stock          INT          NOT NULL,
    file_name      VARCHAR(255),
    content_type   VARCHAR(100),
    file_size      INT,
    file_data      BLOB
);

-- Bridge table for the many-to-many relationship between departments and products.
CREATE TABLE department_products
(
    department_id INT NOT NULL,
    product_id    INT NOT NULL,
    PRIMARY KEY (department_id, product_id),
    FOREIGN KEY (department_id) REFERENCES departments (department_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE
);

-- Seed initial list of departments.
INSERT INTO departments (name, floor, zone, budget, employee_count, is_refrigerated)
VALUES ('Fresh Produce', 0, 1, 15000.00, 8, FALSE),
       ('Bakery', 0, 2, 12000.00, 5, FALSE),
       ('Dairy & Eggs', 1, 3, 18000.00, 6, TRUE),
       ('Meat & Seafood', 1, 4, 25000.00, 10, TRUE),
       ('Frozen Foods', 1, 5, 20000.00, 7, TRUE),
       ('Beverages', 0, 6, 22000.00, 6, FALSE),
       ('Snacks & Confectionery', 0, 13, 17000.00, 5, FALSE),
       ('Snacks & Confectionery Near Checkout', 0, 1, 8000.00, 3, FALSE),
       ('Household & Cleaning', 2, 7, 13000.00, 4, FALSE),
       ('Health & Beauty', 2, 8, 16000.00, 5, FALSE),
       ('Baby & Toddler', 2, 9, 11000.00, 4, FALSE),
       ('International Foods', 1, 10, 14000.00, 6, FALSE);

-- Seed initial product catalog with price, sale status, and stock levels.
INSERT INTO products (name, price, is_on_sale, discount_price, stock)
VALUES ('Heinz Turkish Style Garlic Sauce 420G', 3.45, TRUE, 2.50, 60),
       ('Large Sweet Potatoes Loose Class 1', 0.42, FALSE, NULL, 120),
       ('Little Duck Supreme Quilted Toilet Tissue 16 Mega Rolls', 5.75, FALSE, NULL, 40),
       ('Creamfields Greek Style Natural Yogurt 500G', 0.95, FALSE, NULL, 90),
       ('Courgettes Pre Packed 500G', 1.50, FALSE, NULL, 100),
       ('Relax Camomile & Passionflower 20 Herbal Tea Bags 40G', 1.75, FALSE, NULL, 70),
       ('14 Unsmoked Streaky Bacon Rashers 300G', 2.29, FALSE, NULL, 65),
       ('Curly Fries 700G', 2.20, FALSE, NULL, 75),
       ('Cooked And Peeled King Prawns 150G', 3.75, FALSE, NULL, 45),
       ('30 Tie Handle Bin Liners 50L', 2.00, FALSE, NULL, 55),
       ('Iceberg Lettuce 200G', 0.64, FALSE, NULL, 110),
       ('Potato Farls 6 Pack', 1.50, FALSE, NULL, 85),
       ('Filippo Berio Pure Olive Oil 750Ml', 10.50, FALSE, NULL, 35),
       ('Batiste Original Dry Shampoo 350Ml', 4.50, FALSE, NULL, 50),
       ('Fred & Flo Fragrance Free Wipes 60 Pack', 0.59, FALSE, NULL, 140),
       ('20 Compostable Caddy Liners Tie Top 10L', 1.75, FALSE, NULL, 70),
       ('San Pellegrino Sparkling Natural Mineral Water Multipack 6x1L', 7.00, FALSE, NULL, 30),
       ('Sunblest Pancakes 8 Pack', 1.35, FALSE, NULL, 95),
       ('Heinz Classic Barbecue Sauce 400G', 3.30, TRUE, 2.50, 58),
       ('Mashed Potato 450G', 1.10, FALSE, NULL, 100),
       ('Whole Cucumber Each', 0.99, FALSE, NULL, 105);

-- Seed department-product assignments.
INSERT INTO department_products (department_id, product_id)
VALUES (1, 2),
       (1, 5),
       (1, 11),
       (1, 21),
       (2, 12),
       (2, 18),
       (3, 4),
       (4, 7),
       (4, 9),
       (5, 8),
       (5, 20),
       (6, 6),
       (6, 17),
       (7, 1),
       (7, 19),
       (8, 1),
       (8, 19),
       (9, 3),
       (9, 10),
       (9, 16),
       (10, 14),
       (11, 15),
       (12, 13),
       (12, 1),
       (12, 19);
