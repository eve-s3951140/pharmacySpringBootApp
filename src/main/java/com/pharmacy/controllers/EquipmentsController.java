package com.pharmacy.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.pharmacy.services.EquipmentService;

@Controller
public class EquipmentsController {
    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/equipments")
    public String homepage(Model model) {

        // Get all the equipments from the database
        model.addAttribute("equipments", equipmentService.getAllEquipments());
        
        return "equipments"; // This will return the equipments.html template
    }
}
