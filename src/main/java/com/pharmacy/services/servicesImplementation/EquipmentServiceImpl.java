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

  @Override
  public Collection<Equipment> getAllEquipments() {
    return equipmentRepository.findAll();
  }
}