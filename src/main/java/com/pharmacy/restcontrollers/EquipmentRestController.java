package com.pharmacy.restcontrollers;

import java.util.List;

import com.pharmacy.models.Equipment;
import com.pharmacy.repositories.EquipmentRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

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
