package com.pharmacy.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import com.pharmacy.models.Supplier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;

import com.pharmacy.services.SupplierService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SuppliersController {
    @Autowired
    private SupplierService supplierService;

    // Get all suppliers
    @GetMapping("/suppliers")
    public String homepage(Model model) {

        // Get all the suppliers from the database
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        
        return "suppliers"; // This will return the suppliers.html template
    }

    // Add a new supplier
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

    // Update a supplier
    @PostMapping("/suppliers/update")
    public String updateSupplier(@ModelAttribute Supplier supplier, RedirectAttributes redirectAttributes) {
        try {
            supplierService.updateSupplier(supplier);
            redirectAttributes.addFlashAttribute("message", "Supplier updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating supplier: " + e.getMessage());
        }
        return "redirect:/suppliers"; // Redirect to suppliers.html
    }

    // Delete a supplier
    @GetMapping("/suppliers/delete/{id}")
    public String deleteSupplier(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            supplierService.deleteSupplier(id);
            redirectAttributes.addFlashAttribute("message", "Supplier deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting supplier with ID: " + id + ", due to having associated products");
        }
        return "redirect:/suppliers"; // Redirect to suppliers.html
    }
}
