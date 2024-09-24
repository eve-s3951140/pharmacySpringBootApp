package com.pharmacy.services;

import java.util.Collection;

import com.pharmacy.models.Medicine;

public interface MedicineService {
  // Get all the medicines
  public Collection<Medicine> getAllMedicines();
}