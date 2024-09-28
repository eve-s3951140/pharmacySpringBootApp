package com.pharmacy.controllers.unit;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import com.pharmacy.models.Supplier;
import com.pharmacy.services.SupplierService;
import com.pharmacy.controllers.SuppliersController;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(SuppliersController.class)
class SuppliersControllerUnitTest {
    // Autowire the MockMvc object to be used to perform HTTP requests
    @Autowired
    private MockMvc mockMvc;

    // Mock the SupplierService
    @MockBean
    private SupplierService supplierService;

    // Mock the getAllSuppliers method to return an empty list
    @BeforeEach
    void setUp() {
        when(supplierService.getAllSuppliers()).thenReturn(Collections.emptyList());
    }

    // Test the controller to display the suppliers page
    @Test
    void testDisplayPage() throws Exception {
        mockMvc.perform(get("/suppliers"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("suppliers"))
            .andExpect(view().name("suppliers"));
    }
    
    // Test the controller to add a new supplier
    @Test
    void testAddSupplier_Success() throws Exception {
        mockMvc.perform(post("/suppliers/add")
            .flashAttr("supplier", new Supplier()))
            .andExpect(redirectedUrl("/suppliers"))
            .andExpect(flash().attribute("message", "Supplier added successfully"));

        // Verify that the createSupplier method is called only once
        verify(supplierService, times(1)).createSupplier(any(Supplier.class));
    }

    // Test the controller to add a new supplier and simulate an exception being thrown
    @Test
    void testAddSupplier_Failure() throws Exception {
        // Mock the createSupplier method to throw an exception when called
        doThrow(new RuntimeException("Error adding supplier")).when(supplierService).createSupplier(any(Supplier.class));

        mockMvc.perform(post("/suppliers/add")
                .flashAttr("supplier", new Supplier()))
                .andExpect(redirectedUrl("/suppliers"))
                .andExpect(flash().attribute("errorMessage", "Error adding supplier: Error adding supplier"));

        // Verify that the createSupplier method of supplierService is called once
        verify(supplierService, times(1)).createSupplier(any(Supplier.class));
    }

    // Test the controller to update a supplier
    @Test
    void testUpdateSupplier_Success() throws Exception {
        mockMvc.perform(post("/suppliers/update")
            .flashAttr("supplier", new Supplier()))
            .andExpect(redirectedUrl("/suppliers"))
            .andExpect(flash().attribute("message", "Supplier updated successfully"));

        // Verify that the updateSupplier method is called only once
        verify(supplierService, times(1)).updateSupplier(any(Supplier.class));
    }

    // Test the controller to update a supplier and simulate an exception being thrown
    @Test
    void testUpdateSupplier_Failure() throws Exception {
        // Mock the updateSupplier method to throw an exception when called
        doThrow(new RuntimeException("Error updating supplier")).when(supplierService).updateSupplier(any(Supplier.class));

        mockMvc.perform(post("/suppliers/update")
                .flashAttr("supplier", new Supplier()))
                .andExpect(redirectedUrl("/suppliers"))
                .andExpect(flash().attribute("errorMessage", "Error updating supplier: Error updating supplier"));

        // Verify that the updateSupplier method of supplierService is called once
        verify(supplierService, times(1)).updateSupplier(any(Supplier.class));
    }

    // Test the controller to delete a supplier by ID
    @Test
    void testDeleteSupplier_Success() throws Exception {
        mockMvc.perform(get("/suppliers/delete/1"))
                .andExpect(redirectedUrl("/suppliers"))
                .andExpect(flash().attribute("message", "Supplier deleted successfully"));

        // Verify that the deleteSupplier method is called only once
        verify(supplierService, times(1)).deleteSupplier(1);
    }

    // Test the controller to delete a supplier by ID and simulate an exception being thrown
    @Test
    void testDeleteSupplier_Failure() throws Exception {
        // Mock the deleteSupplier method to throw an exception when called
        doThrow(new RuntimeException("Error deleting supplier")).when(supplierService).deleteSupplier(anyInt());

        mockMvc.perform(get("/suppliers/delete/1"))
                .andExpect(redirectedUrl("/suppliers"))
                .andExpect(flash().attribute("errorMessage", "Error deleting supplier with ID: 1, due to having associated products"));

        // Verify that the deleteSupplier method of supplierService is called once
        verify(supplierService, times(1)).deleteSupplier(1);
    }
}
