package com.pharmacy.restcontrollers;

import java.util.List;

import com.pharmacy.models.Medicine;
import com.pharmacy.repositories.MedicineRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

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
