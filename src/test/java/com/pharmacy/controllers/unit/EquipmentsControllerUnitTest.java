package com.pharmacy.controllers.unit;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import com.pharmacy.models.Equipment;
import com.pharmacy.services.SupplierService;
import com.pharmacy.services.EquipmentService;
import com.pharmacy.controllers.EquipmentsController;

import static org.mockito.Mockito.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(EquipmentsController.class)
class EquipmentsControllerUnitTest {

    // Autowire the MockMvc object to be used to perform HTTP requests
    @Autowired
    private MockMvc mockMvc;

    // Mock the EquipmentService and SupplierService
    @MockBean
    private EquipmentService equipmentService;

    @MockBean
    private SupplierService supplierService;

    // Mock the getAllEquipments and getAllSuppliers methods to return an empty list
    @BeforeEach
    void setUp() {
        when(equipmentService.getAllEquipments()).thenReturn(Collections.emptyList());
        when(supplierService.getAllSuppliers()).thenReturn(Collections.emptyList());
    }

    // Test the controller to display the equipments page
    @Test
    void testDisplayPage() throws Exception {
        mockMvc.perform(get("/equipments"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("equipments"))
                .andExpect(model().attributeExists("suppliers"))
                .andExpect(view().name("equipments"));
    }

    // Test the controller to add a new equipment
    @Test
    void testAddEquipment_Success() throws Exception {
        mockMvc.perform(post("/equipments/add")
                .flashAttr("equipment", new Equipment()))
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("message", "Equipment added successfully"));

        // Verify that the createEquipment method is called only once
        verify(equipmentService, times(1)).createEquipment(any(Equipment.class));
    }

    /**
     * Test the controller to add a new equipment and expect an error when the
     * service throws an exception
     * 
     * @throws Exception
     */
    @Test
    void testAddEquipment_Failure() throws Exception {
        // Mock the createEquipment method to throw a RuntimeException when called
        doThrow(new RuntimeException("Error adding equipment")).when(equipmentService).createEquipment(any());

        mockMvc.perform(post("/equipments/add")
                .flashAttr("equipment", new Equipment()))
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("errorMessage", "Error adding equipment: Error adding equipment"));

        // Verify that the createEquipment method is called only once
        verify(equipmentService, times(1)).createEquipment(any(Equipment.class));
    }

    // Test the controller to update an equipment
    @Test
    void testUpdateEquipment_Success() throws Exception {
        mockMvc.perform(put("/equipments/update")
                .flashAttr("equipment", new Equipment()))
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("message", "Equipment updated successfully"));

        // Verify that the updateEquipment method is called only once
        verify(equipmentService, times(1)).updateEquipment(any(Equipment.class));
    }

    /**
     * Test the controller to update an equipment and expect an error when the
     * service throws an exception
     * 
     * @throws Exception
     */
    @Test
    void testUpdateEquipment_Failure() throws Exception {
        // Mock the updateEquipment method to throw a RuntimeException when called
        doThrow(new RuntimeException("Error updating equipment")).when(equipmentService).updateEquipment(any());

        mockMvc.perform(put("/equipments/update")
                .flashAttr("equipment", new Equipment()))
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("errorMessage", "Error updating equipment: Error updating equipment"));

        // Verify that the updateEquipment method is called only once
        verify(equipmentService, times(1)).updateEquipment(any(Equipment.class));
    }

    // Test the controller to delete an equipment by ID
    @Test
    void testDeleteEquipment_Success() throws Exception {
        mockMvc.perform(delete("/equipments/delete/1"))
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("message", "Equipment deleted successfully"));

        // Verify that the deleteEquipment method is called only once
        verify(equipmentService, times(1)).deleteEquipment(1);
    }

    /**
     * Test the controller to delete an equipment by ID and expect an error when the
     * service throws an exception
     * 
     * @throws Exception
     */
    @Test
    void testDeleteEquipment_Failure() throws Exception {
        // Mock the deleteEquipment method to throw a RuntimeException when called
        doThrow(new RuntimeException("Error deleting equipment")).when(equipmentService).deleteEquipment(anyInt());

        mockMvc.perform(delete("/equipments/delete/1"))
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("errorMessage", "Error deleting equipment with ID: 1"));

        // Verify that the deleteEquipment method is called only once
        verify(equipmentService, times(1)).deleteEquipment(1);
    }
}
