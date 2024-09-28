package com.pharmacy.controllers;

import org.springframework.ui.Model;
import com.pharmacy.models.Equipment;
import com.pharmacy.services.SupplierService;
import com.pharmacy.services.EquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EquipmentsController {
    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private SupplierService supplierService;

    // Get all equipments
    @GetMapping("/equipments")
    public String homepage(Model model) {

        // Get all the equipments from the database
        model.addAttribute("equipments", equipmentService.getAllEquipments());

        // Get all the suppliers from the database
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        
        return "equipments"; // This will return the equipments.html template
    }

    // Add a new equipment
    @PostMapping("/equipments/add")
    public String addMedicine(@ModelAttribute Equipment equipment, RedirectAttributes redirectAttributes) {
        try {
            equipmentService.createEquipment(equipment);
            redirectAttributes.addFlashAttribute("message", "Equipment added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding equipment: " + e.getMessage());
        }
        return "redirect:/equipments"; // Redirect to equipments.html 
    }

}
