package com.pharmacy.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.pharmacy.services.SupplierService;

@Controller
public class SuppliersController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/suppliers")
    public String homepage(Model model) {

        // Get all the suppliers from the database
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        
        return "suppliers"; // This will return the suppliers.html template
    }
}
