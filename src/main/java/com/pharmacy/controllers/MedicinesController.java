package com.pharmacy.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.pharmacy.services.MedicineService;

@Controller
public class MedicinesController {
    @Autowired
    private MedicineService medicineService;

    @GetMapping("/medicines")
    public String homepage(Model model) {

        // Get all the medicines from the database
        model.addAttribute("medicines", medicineService.getAllMedicines());
        
        return "medicines"; // This will return the medicines.html template
    }
}
