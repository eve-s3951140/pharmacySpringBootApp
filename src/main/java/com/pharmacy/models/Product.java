package com.pharmacy.models;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type")
@Table(name = "products")
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @Column(name = "name")
    protected String name;

    @Column(name = "quantity")
    protected int quantity;

    @Column(name = "price")
    protected Double price;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    protected Supplier supplier;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    // Default no-argument constructor required by JPA
    protected Product() {
    }

    // Constructor without ID because it is auto-generated
    protected Product(String name, int quantity, Double price, Supplier supplier) {
        this.name = name;
        this.quantity = quantity;
        this.price = price != null ? price : 0.0; // Assign a default value if null
        this.supplier = supplier;
    }

    public abstract String getProductType(); // Polymorphic method
}