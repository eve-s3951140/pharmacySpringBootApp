package com.pharmacy.controllers;

import org.springframework.ui.Model;
import com.pharmacy.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HomepageController {
    @Autowired
    private ProductService productService;

    @GetMapping("/homepage")
    public String homepage(Model model) {

        // Get all the products from the database
        model.addAttribute("products", productService.getAllProducts());

        return "homepage"; // This will return the homepage.html template
    }
}
