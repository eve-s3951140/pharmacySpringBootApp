package com.pharmacy.controllers;

import org.springframework.ui.Model;
import com.pharmacy.services.EquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

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
