package com.pharmacy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    // Inject JdbcTemplate to perform database operations
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        // Check and insert suppliers if they do not exist
        insertIfNotExists("suppliers", "ABC Pharma", "123");
        insertIfNotExists("suppliers", "XYZ Medical", "456");
        insertIfNotExists("suppliers", "PQR Supplies", "789");
        insertIfNotExists("suppliers", "LMN Equipments", "101112");
        insertIfNotExists("suppliers", "OPQ Instruments", "131415");
        insertIfNotExists("suppliers", "RST Devices", "161718");
        insertIfNotExists("suppliers", "UVW Tools", "192021");

        // Check and insert products if they do not exist
        insertIfNotExistsProduct("Paracetamol", 100, 10.00, "Medicine", 1);
        insertIfNotExistsProduct("Aspirin", 50, 5.00, "Medicine", 1);
        insertIfNotExistsProduct("Ibuprofen", 50, 5.00, "Medicine", 1);
        insertIfNotExistsProduct("Antiseptic", 100, 5.00, "Medicine", 2);
        insertIfNotExistsProduct("Stethoscope", 10, 50.00, "Equipment", 2);
        insertIfNotExistsProduct("Thermometer", 20, 10.00, "Equipment", 2);
        insertIfNotExistsProduct("Syringe", 100, 1.00, "Equipment", 3);
        insertIfNotExistsProduct("Bandage", 200, 2.00, "Equipment", 3);
        insertIfNotExistsProduct("Gloves", 500, 0.50, "Equipment", 3);
        insertIfNotExistsProduct("Scissors", 10, 5.00, "Equipment", 4);
        insertIfNotExistsProduct("Tweezers", 10, 5.00, "Equipment", 4);
        insertIfNotExistsProduct("Needle", 100, 1.00, "Equipment", 4);

        // Check and insert medicines if they do not exist
        insertIfNotExistsMedicine(1, "Pfizer", "2023-12-31");
        insertIfNotExistsMedicine(2, "Bayer", "2023-12-31");
        insertIfNotExistsMedicine(3, "GSK", "2023-12-31");
        insertIfNotExistsMedicine(4, "J&J", "2023-12-31");

        // Check and insert equipments if they do not exist
        insertIfNotExistsEquipment(5, "1 year", "2023-12-31");
        insertIfNotExistsEquipment(6, "2 years", "2023-12-31");
        insertIfNotExistsEquipment(7, "3 years", "2023-12-31");
        insertIfNotExistsEquipment(8, "6 months", "2023-12-31");
        insertIfNotExistsEquipment(9, "4 years", "2023-12-31");
        insertIfNotExistsEquipment(10, "5 years", "2023-12-31");
        insertIfNotExistsEquipment(11, "1 year", "2023-12-31");
        insertIfNotExistsEquipment(12, "2 years", "2023-12-31");
    }

    // Helper methods to insert data if it does not exist
    private void insertIfNotExists(String table, String name, String contact) {
        String sql = "INSERT INTO " + table + " (name, contact) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM " + table + " WHERE name = ?)";
        jdbcTemplate.update(sql, name, contact, name);
    }
 
    // Helper methods to insert data if it does not exist
    private void insertIfNotExistsProduct(String name, int quantity, double price, String productType, int supplierId) {
        String sql = "INSERT INTO products (name, quantity, price, product_type, supplier_id) SELECT ?, ?, ?, ?, ? " +
                     "WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = ?)";
        jdbcTemplate.update(sql, name, quantity, price, productType, supplierId, name);
    }

    // Helper methods to insert data if it does not exist
    private void insertIfNotExistsMedicine(int id, String manufacturer, String expiryDate) {
        String sql = "INSERT INTO medicines (id, manufacturer, expiry_date) SELECT ?, ?, ? " +
                     "WHERE NOT EXISTS (SELECT 1 FROM medicines WHERE id = ?)";
        jdbcTemplate.update(sql, id, manufacturer, expiryDate, id);
    }

    // Helper methods to insert data if it does not exist
    private void insertIfNotExistsEquipment(int id, String warranty, String purchaseDate) {
        String sql = "INSERT INTO equipments (id, warranty, purchase_date) SELECT ?, ?, ? " +
                     "WHERE NOT EXISTS (SELECT 1 FROM equipments WHERE id = ?)";
        jdbcTemplate.update(sql, id, warranty, purchaseDate, id);
    }
}