CREATE TABLE IF NOT EXISTS suppliers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    product_type VARCHAR(31) NOT NULL,
    supplier_id INT NOT NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);


CREATE TABLE IF NOT EXISTS medicines (
    id INT PRIMARY KEY,
    manufacturer VARCHAR(255) NOT NULL,
    expiry_date DATE,
    FOREIGN KEY (id) REFERENCES products(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS equipments (
    id INT PRIMARY KEY,
    warranty VARCHAR(255),
    purchase_date DATE,
    FOREIGN KEY (id) REFERENCES products(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);