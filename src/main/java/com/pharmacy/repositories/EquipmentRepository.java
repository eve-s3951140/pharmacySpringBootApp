package com.pharmacy.repositories;

import org.springframework.stereotype.Repository;

import com.pharmacy.models.Equipment;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
}