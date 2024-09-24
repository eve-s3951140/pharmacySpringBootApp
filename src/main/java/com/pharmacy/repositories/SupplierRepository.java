package com.pharmacy.repositories;

import org.springframework.stereotype.Repository;

import com.pharmacy.models.Supplier;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}