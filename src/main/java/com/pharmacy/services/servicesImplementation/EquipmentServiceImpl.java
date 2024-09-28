package com.pharmacy.services.servicesImplementation;

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

  // Get all the equipments
  @Override
  public Collection<Equipment> getAllEquipments() {
    return equipmentRepository.findAll();
  }

  // Create a new equipment
  @Override
  public void createEquipment(Equipment equipment) {
    // Check if a medicine with the same name, manufacturer, supplier, and expiry date already exists
    Equipment existingeEquipment = equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(
      equipment.getName().trim(),
      equipment.getSupplier(),
      equipment.getPurchaseDate(),
      equipment.getWarranty()
    );

    // If a equipment with the same name, supplier, purchase date, and warranty already exists
    if (existingeEquipment != null) {
      throw new RuntimeException("The equipment with the same name, supplier, purchase date, and warranty already exists");
    }

    // Check if the purchase date is in the future
    if (equipment.getPurchaseDate().isAfter(java.time.LocalDate.now())) {
      throw new RuntimeException("The purchase date cannot be in the future");
    }

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

    equipmentRepository.save(equipment);
  }
}