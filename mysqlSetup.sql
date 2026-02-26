DROP DATABASE IF EXISTS supermarket_store_system;
CREATE DATABASE supermarket_store_system;
USE supermarket_store_system;

CREATE TABLE department(
    department_id INT AUTO_INCREMENT PRIMARY KEY
    # TODO: Hanna
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

# TODO: Inserts