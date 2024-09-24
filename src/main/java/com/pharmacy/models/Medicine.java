package com.pharmacy.models;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Medicine")
@Table(name = "medicines")
public class Medicine extends Product {
    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    // Getters and Setters
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String getProductType() {
        return "Medicine";
    }

    // Default no-argument constructor required by JPA
    public Medicine() {}

    // Constructor does not include ID because it is auto-generated from the Product class
    public Medicine(String name, int quantity, Double price, Supplier supplier, String manufacturer, LocalDate expiryDate) {
        super(name, quantity, price, supplier);
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
    }
}
