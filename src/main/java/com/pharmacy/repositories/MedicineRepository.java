package com.pharmacy.repositories;

import org.springframework.stereotype.Repository;

import com.pharmacy.models.Medicine;
import com.pharmacy.models.Supplier;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    // Find a medicine by name, manufacturer, supplier, and expiry date
    public Medicine findByNameAndManufacturerAndSupplierAndExpiryDate(String name, String manufacturer, Supplier supplier, LocalDate expiryDate);
}