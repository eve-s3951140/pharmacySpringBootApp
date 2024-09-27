package com.pharmacy.restcontrollers;

import java.util.List;

import com.pharmacy.models.Supplier;
import com.pharmacy.repositories.SupplierRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierRestController {
    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    // Get specific supplier by ID
    @GetMapping("/{id}")
    public Supplier getSupplierById(@PathVariable int id) {
        return supplierRepository.findById(id).orElse(null);
    }
}

