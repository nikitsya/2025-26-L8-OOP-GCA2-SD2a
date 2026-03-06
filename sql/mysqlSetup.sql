DROP DATABASE IF EXISTS supermarket_store_system;
CREATE DATABASE supermarket_store_system;
USE supermarket_store_system;

CREATE TABLE departments
(
    department_id INT AUTO_INCREMENT PRIMARY KEY
    # TODO: (Hanna)
);


# drop table if exists products;

CREATE TABLE products
(
    product_id     INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    price          DOUBLE       NOT NULL,
    is_on_sale     BOOLEAN      NOT NULL DEFAULT FALSE,
    discount_price DOUBLE,
    stock          INT          NOT NULL
);

# TODO: (Hanna) department Insert > 10 seed rows

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
       ('Heinz Classic Barbecue Sauce 480G', 3.30, TRUE, 2.50, 58),
       ('Mashed Potato 450G', 1.10, FALSE, NULL, 100),
       ('Whole Cucumber Each', 0.99, FALSE, NULL, 105);
