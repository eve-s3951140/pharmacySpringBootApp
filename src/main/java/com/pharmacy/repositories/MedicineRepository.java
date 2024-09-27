package com.pharmacy.repositories;

import java.time.LocalDate;

import com.pharmacy.models.Medicine;
import com.pharmacy.models.Supplier;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    // Find a medicine by name, manufacturer, supplier, and expiry date
    public Medicine findByNameAndManufacturerAndSupplierAndExpiryDate(String name, String manufacturer, Supplier supplier, LocalDate expiryDate);
}