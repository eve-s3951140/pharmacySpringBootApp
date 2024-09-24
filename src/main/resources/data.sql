-- Delete all data from the tables
DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS suppliers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL
);

INSERT INTO suppliers (name, contact) VALUES 
    ('ABC Pharma', '123'),
    ('XYZ Medical', '456'),
    ('PQR Supplies', '789'),
    ('LMN Equipments', '101112'),
    ('OPQ Instruments', '131415'),
    ('RST Devices', '161718'),
    ('UVW Tools', '192021');

CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    product_type VARCHAR(31) NOT NULL,
    supplier_id INT NOT NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

INSERT INTO products (name, quantity, price, product_type, supplier_id) VALUES 
    ('Paracetamol', 100, 10.00, 'Medicine', 1), 
    ('Aspirin', 50, 5.00, 'Medicine', 1),
    ('Ibuprofen', 50, 5.00, 'Medicine', 1),
    ('Antiseptic', 100, 5.00, 'Medicine', 2), 
    ('Stethoscope', 10, 50.00, 'Equipment', 2),
    ('Thermometer', 20, 10.00, 'Equipment', 2),
    ('Syringe', 100, 1.00, 'Equipment', 3),
    ('Bandage', 200, 2.00, 'Equipment', 3),
    ('Gloves', 500, 0.50, 'Equipment', 3),
    ('Scissors', 10, 5.00, 'Equipment', 4),
    ('Tweezers', 10, 5.00, 'Equipment', 4),
    ('Needle', 100, 1.00, 'Equipment', 4);

CREATE TABLE IF NOT EXISTS medicines (
    id INT PRIMARY KEY,
    manufacturer VARCHAR(255) NOT NULL,
    expiry_date DATE,
    FOREIGN KEY (id) REFERENCES products(id)
);

INSERT INTO medicines (id, manufacturer, expiry_date) VALUES 
    (1, 'Pfizer', '2023-12-31'),
    (2, 'Bayer', '2023-12-31'),
    (3, 'GSK', '2023-12-31'),
    (4, 'J&J', '2023-12-31');

CREATE TABLE IF NOT EXISTS equipments (
    id INT PRIMARY KEY,
    warranty VARCHAR(255),
    purchase_date DATE,
    FOREIGN KEY (id) REFERENCES products(id)
);

INSERT INTO equipments (id, warranty, purchase_date) VALUES 
    (5, '1 year', '2023-12-31'),
    (6, '2 years', '2023-12-31'),
    (7, '3 years', '2023-12-31'),
    (8, '6 months', '2023-12-31'),
    (9, '4 years', '2023-12-31'),
    (10, '5 years', '2023-12-31'),
    (11, '1 year', '2023-12-31'),
    (12, '2 years', '2023-12-31');