package com.pharmacy.repositories;

import org.springframework.stereotype.Repository;

import com.pharmacy.models.Product;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}