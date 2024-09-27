package com.pharmacy.repositories;

import com.pharmacy.models.Equipment;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
}