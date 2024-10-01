CREATE DATABASE order_managemant_sys;

USE order_managemant_sys;

CREATE TABLE clients (
    client_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    address_line_1 VARCHAR(255)  NOT NULL,
    address_line_2 VARCHAR(255)  NOT NULL,
    address_line_3 VARCHAR(255),
    status ENUM('NEW', 'DISPATCHED', 'CANCELLED') DEFAULT 'NEW',
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE
);