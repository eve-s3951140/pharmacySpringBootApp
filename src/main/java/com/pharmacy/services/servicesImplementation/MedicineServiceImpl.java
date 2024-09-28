package com.pharmacy.services.servicesImplementation;

import java.time.LocalDate;
import java.util.Collection;

import com.pharmacy.models.Medicine;
import com.pharmacy.services.MedicineService;
import com.pharmacy.repositories.MedicineRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MedicineServiceImpl implements MedicineService {

  private MedicineRepository medicineRepository;

  @Autowired
  public MedicineServiceImpl(MedicineRepository medicineRepository) {
    this.medicineRepository = medicineRepository;
  }

  // Validation helper methods
  private void checkMedicineFields(Medicine medicine) {
    if (medicine.getSupplier() == null) {
      throw new RuntimeException("The supplier does not exist");
    }
    
    if (medicine.getPrice() < 0 || medicine.getPrice() == null) {
      throw new RuntimeException("The price cannot be negative");
    }

    if (medicine.getQuantity() < 0) {
      throw new RuntimeException("The quantity cannot be negative");
    }

    if (medicine.getExpiryDate().isBefore(LocalDate.now())) {
      throw new RuntimeException("The expiry date cannot be in the past");
    }
  }

  // Check if a medicine with the same name, supplier, expiry date, and manufacturer
  private void checkForDuplicateMedicine(Medicine medicine) {
    Medicine existingMedicine = medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(
        medicine.getName().trim(),
        medicine.getManufacturer().trim(),
        medicine.getSupplier(),
        medicine.getExpiryDate());

    if (existingMedicine != null) {
      throw new RuntimeException(
          "The medicine with the same name, supplier, expiry date, and manufacturer already exists");
    }
  }

  // Get all the medicines
  @Override
  public Collection<Medicine> getAllMedicines() {
    return medicineRepository.findAll();
  }

  // Create a new medicine
  @Override
  public void createMedicine(Medicine medicine) {
    checkMedicineFields(medicine);
    checkForDuplicateMedicine(medicine);
    medicineRepository.save(medicine);
  }

  // Update a medicine
  @Override
  public void updateMedicine(Medicine medicine) {
    // Check if the medicine exists
    Medicine existingMedicine = medicineRepository.findById(medicine.getId()).orElse(null);

    // If the medicine does not exist
    if (existingMedicine == null) {
      throw new RuntimeException("The medicine does not exist");
    }

    // Check if the supplier does not exist
    checkMedicineFields(medicine);

    // Check if a medicine with the same name, manufacturer, supplier, and expiry
    // date already exists
    Medicine duplicateMedicine = medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(
        medicine.getName().trim(),
        medicine.getManufacturer().trim(),
        medicine.getSupplier(),
        medicine.getExpiryDate());

    // If a medicine with the same name, manufacturer, supplier, and expiry date
    // already exists
    if (duplicateMedicine != null && duplicateMedicine.getId() != medicine.getId()) {
      throw new RuntimeException(
          "The medicine with the same name, manufacturer, supplier, and expiry date already exists");
    }

    medicineRepository.save(medicine);
  }

  // Delete a medicine
  @Override
  public void deleteMedicine(int id) {
    // Check if the medicine exists
    Medicine existingMedicine = medicineRepository.findById(id).orElse(null);

    // If the medicine does not exist
    if (existingMedicine == null) {
      throw new RuntimeException("The medicine does not exist");
    }

    medicineRepository.deleteById(id);
  }
}