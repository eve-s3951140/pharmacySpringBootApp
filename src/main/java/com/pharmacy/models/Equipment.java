package com.pharmacy.models;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Equipment")
@Table(name = "equipments")
public class Equipment extends Product {
    @Column(name = "warranty")
    private String warranty;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    // Getters and Setters
    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String getProductType() {
        return "Equipment";
    }

    // Default no-argument constructor required by JPA
    public Equipment() {
    }

    // Constructor does not include ID because it is auto-generated from the Product class
    public Equipment(String warranty, LocalDate purchaseDate, String name, int quantity, Double price, Supplier supplier) {
        super(name, quantity, price, supplier);
        this.warranty = warranty;
        this.purchaseDate = purchaseDate;
    }
}