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

  @Override
  public Collection<Medicine> getAllMedicines() {
    return medicineRepository.findAll();
  }
}