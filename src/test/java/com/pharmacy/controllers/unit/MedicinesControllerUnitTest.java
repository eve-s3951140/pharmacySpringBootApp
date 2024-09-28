package com.pharmacy.controllers.unit;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

import com.pharmacy.models.Medicine;
import com.pharmacy.services.SupplierService;
import com.pharmacy.services.MedicineService;
import com.pharmacy.controllers.MedicinesController;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MedicinesController.class)
class MedicinesControllerUnitTest {

    // Autowire the MockMvc object to be used to perform HTTP requests
    @Autowired
    private MockMvc mockMvc;

    // Mock the MedicineService and SupplierService
    @MockBean
    private MedicineService medicineService;

    @MockBean
    private SupplierService supplierService;

    // Mock the getAllMedicines and getAllSuppliers methods to return an empty list
    @BeforeEach
    void setUp() {
        when(medicineService.getAllMedicines()).thenReturn(Collections.emptyList());
        when(supplierService.getAllSuppliers()).thenReturn(Collections.emptyList());
    }

    // Test the controller to display the medicines page
    @Test
    void testDisplayPage() throws Exception {
        mockMvc.perform(get("/medicines"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicines"))
            .andExpect(model().attributeExists("suppliers"))
            .andExpect(view().name("medicines"));
    }

    // Test the controller to add a new medicine
    @Test
    void testAddMedicine_Success() throws Exception {
        mockMvc.perform(post("/medicines/add")
            .flashAttr("medicine", new Medicine()))
            .andExpect(redirectedUrl("/medicines"))
            .andExpect(flash().attribute("message", "Medicine added successfully"));

        // Verify that the createMedicine method is called only once
        verify(medicineService, times(1)).createMedicine(any(Medicine.class));
    }

    // Test the controller to add a new medicine and expect an error when the service throws an exception
    @Test
    void testAddMedicine_Failure() throws Exception {
        // Mock the createMedicine method to throw a RuntimeException when called
        doThrow(new RuntimeException("Error adding medicine")).when(medicineService).createMedicine(any());

        mockMvc.perform(post("/medicines/add")
                .flashAttr("medicine", new Medicine()))
                .andExpect(redirectedUrl("/medicines"))
                .andExpect(flash().attribute("errorMessage", "Error adding medicine: Error adding medicine"));

        // Verify that the createMedicine method is called only once
        verify(medicineService, times(1)).createMedicine(any(Medicine.class));
    }

    // Test the controller to update a medicine
    @Test
    void testUpdateMedicine_Success() throws Exception {
        mockMvc.perform(post("/medicines/update")
            .flashAttr("medicine", new Medicine()))
            .andExpect(redirectedUrl("/medicines"))
            .andExpect(flash().attribute("message", "Medicine updated successfully"));

        // Verify that the updateMedicine method is called only once
        verify(medicineService, times(1)).updateMedicine(any(Medicine.class));
    }

    // Test the controller to update a medicine and expect an error when the service throws an exception
    @Test
    void testUpdateMedicine_Failure() throws Exception {
        // Mock the updateMedicine method to throw a RuntimeException when called
        doThrow(new RuntimeException("Error updating medicine")).when(medicineService).updateMedicine(any());

        mockMvc.perform(post("/medicines/update")
                .flashAttr("medicine", new Medicine()))
                .andExpect(redirectedUrl("/medicines"))
                .andExpect(flash().attribute("errorMessage", "Error updating medicine: Error updating medicine"));

        // Verify that the updateMedicine method is called only once
        verify(medicineService, times(1)).updateMedicine(any(Medicine.class));
    }

    // Test the controller to delete an medicine by ID
    @Test
    void testDeleteMedicine_Success() throws Exception {
        mockMvc.perform(get("/medicines/delete/1"))
                .andExpect(redirectedUrl("/medicines"))
                .andExpect(flash().attribute("message", "Medicine deleted successfully"));

        // Verify that the deleteMedicine method is called only once
        verify(medicineService, times(1)).deleteMedicine(1);
    }

    // Test the controller to delete an medicine by ID and expect an error when the service throws an exception
    @Test
    void testDeleteMedicine_Failure() throws Exception {
        // Mock the deleteMedicine method to throw a RuntimeException when called
        doThrow(new RuntimeException("Error deleting medicine")).when(medicineService).deleteMedicine(anyInt());

        mockMvc.perform(get("/medicines/delete/1"))
                .andExpect(redirectedUrl("/medicines"))
                .andExpect(flash().attribute("errorMessage", "Error deleting medicine with ID: 1"));

        // Verify that the deleteMedicine method is called only once
        verify(medicineService, times(1)).deleteMedicine(1);
    }
}
