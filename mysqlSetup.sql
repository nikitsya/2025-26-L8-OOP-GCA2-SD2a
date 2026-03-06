DROP DATABASE IF EXISTS supermarket_store_system;
CREATE DATABASE supermarket_store_system;
USE supermarket_store_system;

CREATE TABLE departments
(
    department_id INT AUTO_INCREMENT PRIMARY KEY
    # TODO: (Hanna)
);

CREATE TABLE products
(
    product_id     INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    price          DOUBLE       NOT NULL,
    is_on_sale     BOOLEAN      NOT NULL DEFAULT FALSE,
    discount_price DOUBLE,
    stock          INT          NOT NULL,
    department_id  INT,
    FOREIGN KEY (department_id) REFERENCES departments (department_id)
);

# TODO: (Hanna) department Insert > 10 seed rows

INSERT INTO products (name, price, is_on_sale, discount_price, stock, department_id)
VALUES
    ('Heinz Turkish Style Garlic Sauce 420G', 3.45, TRUE, 2.50, 60, NULL),
    ('Large Sweet Potatoes Loose Class 1', 0.42, FALSE, NULL, 120, NULL),
    ('Little Duck Supreme Quilted Toilet Tissue 16 Mega Rolls', 5.75, FALSE, NULL, 40, NULL),
    ('Creamfields Greek Style Natural Yogurt 500G', 0.95, FALSE, NULL, 90, NULL),
    ('Courgettes Pre Packed 500G', 1.50, FALSE, NULL, 100, NULL),
    ('Relax Camomile & Passionflower 20 Herbal Tea Bags 40G', 1.75, FALSE, NULL, 70, NULL),
    ('14 Unsmoked Streaky Bacon Rashers 300G', 2.29, FALSE, NULL, 65, NULL),
    ('Curly Fries 700G', 2.20, FALSE, NULL, 75, NULL),
    ('Cooked And Peeled King Prawns 150G', 3.75, FALSE, NULL, 45, NULL),
    ('30 Tie Handle Bin Liners 50L', 2.00, FALSE, NULL, 55, NULL),
    ('Iceberg Lettuce 200G', 0.64, FALSE, NULL, 110, NULL),
    ('Potato Farls 6 Pack', 1.50, FALSE, NULL, 85, NULL),
    ('Filippo Berio Pure Olive Oil 750Ml', 10.50, FALSE, NULL, 35, NULL),
    ('Batiste Original Dry Shampoo 350Ml', 4.50, FALSE, NULL, 50, NULL),
    ('Fred & Flo Fragrance Free Wipes 60 Pack', 0.59, FALSE, NULL, 140, NULL),
    ('20 Compostable Caddy Liners Tie Top 10L', 1.75, FALSE, NULL, 70, NULL),
    ('San Pellegrino Sparkling Natural Mineral Water Multipack 6x1L', 7.00, FALSE, NULL, 30, NULL),
    ('Sunblest Pancakes 8 Pack', 1.35, FALSE, NULL, 95, NULL),
    ('Heinz Classic Barbecue Sauce 480G', 3.30, TRUE, 2.50, 58, NULL),
    ('Mashed Potato 450G', 1.10, FALSE, NULL, 100, NULL),
    ('Whole Cucumber Each', 0.99, FALSE, NULL, 105, NULL);
