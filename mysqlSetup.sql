DROP DATABASE IF EXISTS supermarket_store_system;
CREATE DATABASE supermarket_store_system;
USE supermarket_store_system;

CREATE TABLE departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY
    # TODO: (Hanna)
);

CREATE TABLE products (
     product_id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(100) NOT NULL,
     price DOUBLE NOT NULL,
     is_on_sale BOOLEAN NOT NULL DEFAULT FALSE,
     discount_price DOUBLE,
     stock INT NOT NULL,
     department_id INT,
     FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

# TODO: (Hanna) department Insert > 10 seed rows

# TODO: (Nikita) product Insert > 10 seed rows