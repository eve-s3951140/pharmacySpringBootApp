package com.pharmacy.services;

import java.util.Collection;

import com.pharmacy.models.Equipment;

public interface EquipmentService {
  // Get all the equipments
  public Collection<Equipment> getAllEquipments();

  // Create a new equipment
  public void createEquipment(Equipment equipment);

  // Update an equipment
  public void updateEquipment(Equipment equipment);
}