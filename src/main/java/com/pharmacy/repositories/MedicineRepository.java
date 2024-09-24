package com.pharmacy.repositories;

import org.springframework.stereotype.Repository;

import com.pharmacy.models.Medicine;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
}