package com.pharmacy.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataInitializer {

    // Inject JdbcTemplate to perform database operations
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        // Check and insert suppliers if no suppliers exist
        if (!isSuppliersExist()) {
            insertSupplier("ABC Pharma", "0412678947");
            insertSupplier("XYZ Medical", "0498563728");
            insertSupplier("PQR Supplies", "0417010404");
            insertSupplier("LMN Equipments", "0415102004");
            insertSupplier("OPQ Instruments", "0407111755");
            insertSupplier("RST Devices", "0493864123");
        }

        // Check and insert products if no products exist
        if (!isProductsExist()) {
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
        }

        // Check and insert medicines if no medicines exist
        if (!isMedicinesExist()) {
            insertIfNotExistsMedicine(1, "Pfizer", "2023-12-31");
            insertIfNotExistsMedicine(2, "Bayer", "2023-12-31");
            insertIfNotExistsMedicine(3, "GSK", "2023-12-31");
            insertIfNotExistsMedicine(4, "J&J", "2023-12-31");
        }

        // Check and insert equipments if no equipments exist
        if (!isEquipmentsExist()) {
            insertIfNotExistsEquipment(5, "1 year", "2023-12-31");
            insertIfNotExistsEquipment(6, "2 years", "2023-12-31");
            insertIfNotExistsEquipment(7, "3 years", "2023-12-31");
            insertIfNotExistsEquipment(8, "6 months", "2023-12-31");
            insertIfNotExistsEquipment(9, "4 years", "2023-12-31");
            insertIfNotExistsEquipment(10, "5 years", "2023-12-31");
            insertIfNotExistsEquipment(11, "1 year", "2023-12-31");
            insertIfNotExistsEquipment(12, "2 years", "2023-12-31");
        }
    }

    // Helper method to check if suppliers exist
    private boolean isSuppliersExist() {
        String sql = "SELECT COUNT(*) FROM suppliers";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null && count > 0;
    }

    // Helper method to insert supplier
    private void insertSupplier(String name, String contact) {
        String sql = "INSERT INTO suppliers (name, contact) VALUES (?, ?)";
        jdbcTemplate.update(sql, name, contact);
    }
    
    // Helper method to check if products exist
    private boolean isProductsExist() {
        String sql = "SELECT COUNT(*) FROM products";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null && count > 0;
    }

    // Helper method to insert data if it does not exist
    private void insertIfNotExistsProduct(String name, int quantity, double price, String productType, int supplierId) {
        String sql = "INSERT INTO products (name, quantity, price, product_type, supplier_id) SELECT ?, ?, ?, ?, ? " +
                     "WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = ?)";
        jdbcTemplate.update(sql, name, quantity, price, productType, supplierId, name);
    }

    // Helper method to check if medicines exist
    private boolean isMedicinesExist() {
        String sql = "SELECT COUNT(*) FROM medicines";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null && count > 0;
    }

    // Helper method to insert data if it does not exist
    private void insertIfNotExistsMedicine(int id, String manufacturer, String expiryDate) {
        String sql = "INSERT INTO medicines (id, manufacturer, expiry_date) SELECT ?, ?, ? " +
                     "WHERE NOT EXISTS (SELECT 1 FROM medicines WHERE id = ?)";
        jdbcTemplate.update(sql, id, manufacturer, expiryDate, id);
    }

    // Helper method to check if equipments exist
    private boolean isEquipmentsExist() {
        String sql = "SELECT COUNT(*) FROM equipments";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null && count > 0;
    }

    // Helper method to insert data if it does not exist
    private void insertIfNotExistsEquipment(int id, String warranty, String purchaseDate) {
        String sql = "INSERT INTO equipments (id, warranty, purchase_date) SELECT ?, ?, ? " +
                     "WHERE NOT EXISTS (SELECT 1 FROM equipments WHERE id = ?)";
        jdbcTemplate.update(sql, id, warranty, purchaseDate, id);
    }
}