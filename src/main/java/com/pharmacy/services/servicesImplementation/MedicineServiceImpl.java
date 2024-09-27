package com.pharmacy.services.servicesImplementation;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.pharmacy.models.Medicine;
import com.pharmacy.repositories.MedicineRepository;
import com.pharmacy.services.MedicineService;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MedicineServiceImpl implements MedicineService {

  private MedicineRepository medicineRepository;

  @Autowired
  public MedicineServiceImpl(MedicineRepository medicineRepository) {
    this.medicineRepository = medicineRepository;
  }

  // Get all the medicines
  @Override
  public Collection<Medicine> getAllMedicines() {
    return medicineRepository.findAll();
  }

  // Create a new medicine
  @Override
  public void createMedicine(Medicine medicine) {
    // Check if a medicine with the same name, manufacturer, supplier, and expiry date already exists
    Medicine existingMedicine = medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(
        medicine.getName().trim(),
        medicine.getManufacturer().trim(),
        medicine.getSupplier(),
        medicine.getExpiryDate()
    );

    // If a medicine with the same name, manufacturer, supplier, and expiry date already exists
    if (existingMedicine != null) {
      throw new RuntimeException("The medicine with the same name, manufacturer, supplier, and expiry date already exists");
    }

    // Check if the expiry date is in the past
    if (medicine.getExpiryDate().isBefore(java.time.LocalDate.now())) {
      throw new RuntimeException("The expiry date cannot be in the past");
    }

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

    // Check if a medicine with the same name, manufacturer, supplier, and expiry date already exists
    Medicine duplicateMedicine = medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(
        medicine.getName().trim(),
        medicine.getManufacturer().trim(),
        medicine.getSupplier(),
        medicine.getExpiryDate()
    );

    // If a medicine with the same name, manufacturer, supplier, and expiry date already exists
    if (duplicateMedicine != null && duplicateMedicine.getId() != medicine.getId()) {
      throw new RuntimeException("The medicine with the same name, manufacturer, supplier, and expiry date already exists");
    }

    // Check if the expiry date is in the past
    if (medicine.getExpiryDate().isBefore(java.time.LocalDate.now())) {
      throw new RuntimeException("The expiry date cannot be in the past");
    }

    medicineRepository.save(medicine);
  }
}