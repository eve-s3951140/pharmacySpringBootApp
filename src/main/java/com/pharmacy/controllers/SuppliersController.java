package com.pharmacy.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import com.pharmacy.models.Supplier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;

import com.pharmacy.services.SupplierService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/suppliers/add")
    public String addSupplier(@ModelAttribute Supplier supplier, RedirectAttributes redirectAttributes) {
        try {
            supplierService.createSupplier(supplier);
            redirectAttributes.addFlashAttribute("message", "Supplier added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding supplier: " + e.getMessage());
        }
        return "redirect:/suppliers"; // Redirect to suppliers.html 
    }
}
