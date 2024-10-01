package com.pharmacy.services.servicesImplementation;

import java.time.LocalDate;
import java.util.Collection;

import com.pharmacy.models.Equipment;
import com.pharmacy.services.EquipmentService;
import com.pharmacy.repositories.EquipmentRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EquipmentServiceImpl implements EquipmentService {

  private EquipmentRepository equipmentRepository;

  @Autowired
  public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
    this.equipmentRepository = equipmentRepository;
  }

  // Validation helper methods
  private void checkEquipmentFields(Equipment equipment) {
    if (equipment.getSupplier() == null) {
      throw new RuntimeException("The supplier does not exist");
    }

    if (equipment.getPrice() < 0 || equipment.getPrice() == null) {
      throw new RuntimeException("The price cannot be negative");
    }

    if (equipment.getQuantity() < 0) {
      throw new RuntimeException("The quantity cannot be negative");
    }

    if (equipment.getPurchaseDate().isAfter(LocalDate.now())) {
      throw new RuntimeException("The purchase date cannot be in the future");
    }
  }

  // Check if a equipment with the same name, supplier, purchase date, and
  // warranty
  private void checkForDuplicateEquipment(Equipment equipment) {
    Equipment existingEquipment = equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(
        equipment.getName().trim(),
        equipment.getSupplier(),
        equipment.getPurchaseDate(),
        equipment.getWarranty());

    if (existingEquipment != null) {
      throw new RuntimeException(
          "The equipment with the same name, supplier, purchase date, and warranty already exists");
    }
  }

  // Get all the equipments
  @Override
  public Collection<Equipment> getAllEquipments() {
    return equipmentRepository.findAll();
  }

  // Create a new equipment
  @Override
  public void createEquipment(Equipment equipment) {
    checkEquipmentFields(equipment);
    checkForDuplicateEquipment(equipment);
    equipmentRepository.save(equipment);
  }

  // Update an equipment
  @Override
  public void updateEquipment(Equipment equipment) {
    // Check if the equipment exists
    Equipment existingEquipment = equipmentRepository.findById(equipment.getId()).orElse(null);

    // If the equipment does not exist
    if (existingEquipment == null) {
      throw new RuntimeException("The equipment does not exist");
    }

    // Check the fields of the equipment
    checkEquipmentFields(equipment);

    // Check if a equipment with the same name, supplier, purchase date, and
    // warranty already exists
    Equipment duplicateEquipment = equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(
        equipment.getName().trim(),
        equipment.getSupplier(),
        equipment.getPurchaseDate(),
        equipment.getWarranty());

    // If a equipment with the same name, supplier, purchase date, and warranty
    // already exists
    if (duplicateEquipment != null && duplicateEquipment.getId() != equipment.getId()) {
      throw new RuntimeException(
          "The equipment with the same name, supplier, purchase date, and warranty already exists");
    }

    equipmentRepository.save(equipment);
  }

  // Delete an equipment
  @Override
  public void deleteEquipment(int id) {
    // Check if the equipment exists
    Equipment existingEquipment = equipmentRepository.findById(id).orElse(null);

    // If the equipment does not exist
    if (existingEquipment == null) {
      throw new RuntimeException("The equipment does not exist");
    }

    equipmentRepository.deleteById(id);
  }
}