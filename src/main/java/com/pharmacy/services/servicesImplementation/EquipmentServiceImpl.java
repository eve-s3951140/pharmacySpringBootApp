package com.pharmacy.services.servicesImplementation;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.pharmacy.models.Equipment;
import com.pharmacy.repositories.EquipmentRepository;
import com.pharmacy.services.EquipmentService;

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