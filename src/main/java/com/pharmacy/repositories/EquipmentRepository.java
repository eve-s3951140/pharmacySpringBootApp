package com.pharmacy.repositories;

import java.time.LocalDate;

import com.pharmacy.models.Supplier;
import com.pharmacy.models.Equipment;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    // Find an equipment by name, supplier, purchase date, and warranty
    public Equipment findByNameAndSupplierAndPurchaseDateAndWarranty(String name, Supplier supplier, LocalDate purchaseDate, String warranty);
}