package com.pharmacy.restcontrollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.models.Medicine;
import com.pharmacy.repositories.MedicineRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineRestController {
    @Autowired
    private MedicineRepository medicineRepository;

    @GetMapping
    public List<Medicine> getMedicines() {
        return medicineRepository.findAll();
    }
}
