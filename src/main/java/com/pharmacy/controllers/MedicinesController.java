package com.pharmacy.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import com.pharmacy.models.Medicine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pharmacy.services.MedicineService;
import com.pharmacy.services.SupplierService;

@Controller
public class MedicinesController {
    @Autowired
    private MedicineService medicineService;

    @Autowired
    private SupplierService supplierService;

    // Get all medicines
    @GetMapping("/medicines")
    public String homepage(Model model) {

        // Get all the medicines from the database
        model.addAttribute("medicines", medicineService.getAllMedicines());

        // Get all the suppliers from the database
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        
        return "medicines"; // This will return the medicines.html template
    }

    // Add a new medicine
    @PostMapping("/medicines/add")
    public String addMedicine(@ModelAttribute Medicine medicine, RedirectAttributes redirectAttributes) {
        try {
            medicineService.createMedicine(medicine);
            redirectAttributes.addFlashAttribute("message", "Medicine added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding medicine: " + e.getMessage());
        }
        return "redirect:/medicines"; // Redirect to medicines.html 
    }

    // Update a medicine
    @PostMapping("/medicines/update")
    public String updateMedicine(@ModelAttribute Medicine medicine, RedirectAttributes redirectAttributes) {
        try {
            medicineService.updateMedicine(medicine);
            redirectAttributes.addFlashAttribute("message", "Medicine updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating medicine: " + e.getMessage());
        }
        return "redirect:/medicines"; // Redirect to medicines.html
    }
}
