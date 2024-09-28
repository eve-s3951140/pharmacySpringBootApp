package com.pharmacy;

import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DatabaseCleaner {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clean() {
        try {
            // Delete all records from the products table and suppliers table
            jdbcTemplate.update("DELETE FROM products");
            jdbcTemplate.update("DELETE FROM suppliers");

            // Reset the auto-increment value
            jdbcTemplate.update("ALTER TABLE products ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.update("ALTER TABLE suppliers ALTER COLUMN id RESTART WITH 1");
        } catch (Exception e) {
            System.err.println("Error cleaning database: " + e.getMessage());
            // Handle exception (logging, rethrowing, etc.)
        }
    }
}