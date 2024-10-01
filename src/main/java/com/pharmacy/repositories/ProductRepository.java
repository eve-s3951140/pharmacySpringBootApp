package com.pharmacy.repositories;

import java.util.Collection;

import com.pharmacy.models.Product;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Find all products by supplier id
    Collection<Product> findBySupplierId(int supplierId);
}