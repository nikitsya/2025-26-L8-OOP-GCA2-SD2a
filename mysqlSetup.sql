DROP DATABASE IF EXISTS SupermarketStoreSystem;
CREATE DATABASE SupermarketStoreSystem;
USE SupermarketStoreSystem;

CREATE TABLE department(
    department_id INT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE product (
     product_id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(100) NOT NULL,
     price DOUBLE NOT NULL,
     is_on_sale BOOLEAN NOT NULL DEFAULT FALSE,
     discount_price DOUBLE,
     stock INT NOT NULL,
     department_id INT,
     FOREIGN KEY (department_id) REFERENCES department(department_id)
);