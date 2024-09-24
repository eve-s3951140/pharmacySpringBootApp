package com.pharmacy.restcontrollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.models.Equipment;
import com.pharmacy.repositories.EquipmentRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentRestController {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @GetMapping
    public List<Equipment> getEquipments() {
        return equipmentRepository.findAll();
    }
}
