package com.pharmacy.services;

import java.util.Collection;

import com.pharmacy.models.Medicine;

public interface MedicineService {
  // Get all the medicines
  public Collection<Medicine> getAllMedicines();

  // Create a new medicine
  public void createMedicine(Medicine medicine);

  // Update a medicine
  public void updateMedicine(Medicine medicine);

  // Delete a medicine
  public void deleteMedicine(int id);
}