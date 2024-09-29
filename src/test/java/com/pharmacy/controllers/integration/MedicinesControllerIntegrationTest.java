package com.pharmacy.controllers.integration;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.pharmacy.DatabaseCleaner;
import com.pharmacy.models.Medicine;
import com.pharmacy.models.Supplier;
import com.pharmacy.services.MedicineService;
import com.pharmacy.services.SupplierService;
import com.pharmacy.repositories.MedicineRepository;

@Rollback
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @AfterAll
class MedicinesControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    // Reset the database to initial values after all tests are done
    @AfterAll
    void tearDownAll() {
        databaseCleaner.clean();
    }

    // Test the controller to add a new medicine
    @Test
    void testAddMedicine_Success() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create the equipment object with all required parameters
        Medicine medicine = new Medicine("Test Medicine", 100, 10.0, supplier, "Test Manufacturer", LocalDate.now());

        // Perform a POST request to add the medicine
        mockMvc.perform(MockMvcRequestBuilders.post("/medicines/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", medicine.getName())
                .param("quantity", String.valueOf(medicine.getQuantity()))
                .param("price", String.valueOf(medicine.getPrice()))
                .param("supplier", String.valueOf(medicine.getSupplier().getId()))
                .param("manufacturer", medicine.getManufacturer())
                .param("expiryDate", medicine.getExpiryDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicines"))
                .andExpect(flash().attribute("message", "Medicine added successfully"));

        // Check if the medicine is added to the database
        Medicine savedMedicine = medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(
                medicine.getName().trim(),
                medicine.getManufacturer().trim(),
                medicine.getSupplier(),
                medicine.getExpiryDate());

        // Check if the medicine has the correct values
        assertAll("medicine",
                () -> assertNotNull(savedMedicine, "Medicine was not saved successfully"),
                () -> assertEquals(medicine.getName(), savedMedicine.getName(), "Medicine name is incorrect"),
                () -> assertEquals(medicine.getQuantity(), savedMedicine.getQuantity(),
                        "Medicine quantity is incorrect"),
                () -> assertEquals(medicine.getPrice(), savedMedicine.getPrice(), "Medicine price is incorrect"),
                () -> assertEquals(medicine.getSupplier(), savedMedicine.getSupplier(),
                        "Medicine supplier is incorrect"),
                () -> assertEquals(medicine.getManufacturer(), savedMedicine.getManufacturer(),
                        "Medicine manufacturer is incorrect"),
                () -> assertEquals(medicine.getExpiryDate(), savedMedicine.getExpiryDate(),
                        "Medicine expiry date is incorrect"));
    }

    /*
     * Test the controller to add a new medicine and expect an error when the
     * service throws an exception for duplicate medicine
     */
    @Test
    void testAddMedicine_Failure_WhenDuplicateEquipment() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create the medicine object with all required parameters
        Medicine medicine = new Medicine("Test Medicine", 100, 10.0, supplier, "Test Manufacturer", LocalDate.now());

        // Mock the createMedicine method to throw a RuntimeException when called
        medicineService.createMedicine(medicine);

        // Perform the POST request, simulating form submission with parameters
        mockMvc.perform(MockMvcRequestBuilders.post("/medicines/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", medicine.getName())
                .param("quantity", String.valueOf(medicine.getQuantity()))
                .param("price", String.valueOf(medicine.getPrice()))
                .param("manufacturer", medicine.getManufacturer())
                .param("expiryDate", medicine.getExpiryDate().toString())
                .param("supplier", String.valueOf(medicine.getSupplier().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicines"))
                .andExpect(flash().attribute("errorMessage",
                        "Error adding medicine: The medicine with the same name, supplier, expiry date, and manufacturer already exists"));
    }

    /*
     * Test the controller to add a new medicine and expect an error when the
     * service throws an exception for a negative price
     */
    @Test
    void testAddMedicine_Failure_WhenPriceIsNegative() {
        Medicine medicine = new Medicine();
        medicine.setPrice(-1.0); // Set a negative price
        medicine.setQuantity(10);
        medicine.setExpiryDate(LocalDate.now());
        medicine.setSupplier(new Supplier("Test Supplier", "123456"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicineService.createMedicine(medicine);
        });
        assertEquals("The price cannot be negative", exception.getMessage());
    }

    /*
     * Test the controller to add a new medicine and expect an error when the
     * service throws an exception for a negative quantity
     */
    @Test
    void testAddMedicine_Failure_WhenQuantityIsNegative() {
        Medicine medicine = new Medicine();
        medicine.setPrice(10.0);
        medicine.setQuantity(-1); // Set a negative quantity
        medicine.setExpiryDate(LocalDate.now());
        medicine.setSupplier(new Supplier("Test Supplier", "123456"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicineService.createMedicine(medicine);
        });
        assertEquals("The quantity cannot be negative", exception.getMessage());
    }

    /*
     * Test the controller to add a new medicine and expect an error when the
     * service throws an exception for an expiry date in the past
     */
    @Test
    void testAddMedicine_Failure_WhenExpiryDateIsInPast() {
        Medicine medicine = new Medicine();
        medicine.setPrice(10.0);
        medicine.setQuantity(10);
        medicine.setExpiryDate(LocalDate.now().minusDays(200)); // Set an expiry date in the past
        medicine.setSupplier(new Supplier("Test Supplier", "123456"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicineService.createMedicine(medicine);
        });
        assertEquals("The expiry date cannot be in the past", exception.getMessage());
    }

    /*
     * Test the controller to add a new medicine and expect an error when the
     * service throws an exception for a null supplier
     */
    @Test
    void testCreateMedicine_Failure_WhenSupplierIsNull() {
        Medicine medicine = new Medicine();
        medicine.setPrice(199.99);
        medicine.setQuantity(10);
        medicine.setExpiryDate(LocalDate.now());
        medicine.setSupplier(null); // Set supplier to null

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicineService.createMedicine(medicine);
        });
        assertEquals("The supplier does not exist", exception.getMessage());
    }

    // Test the controller to update a medicine
    @Test
    void testUpdateMedicine_Success() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create the medicine object with all required parameters
        Medicine medicine = new Medicine("Test Medicine", 100, 10.0, supplier, "Test Manufacturer", LocalDate.now());
        medicineService.createMedicine(medicine);

        // Update the medicine object with new values
        medicine.setName("Updated Medicine");
        medicine.setQuantity(200);
        medicine.setPrice(20.0);
        medicine.setManufacturer("Updated Manufacturer");
        medicine.setExpiryDate(LocalDate.now().plusDays(100));

        // Perform a POST request to update the medicine
        mockMvc.perform(MockMvcRequestBuilders.post("/medicines/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", String.valueOf(medicine.getId()))
                .param("name", medicine.getName())
                .param("quantity", String.valueOf(medicine.getQuantity()))
                .param("price", String.valueOf(medicine.getPrice()))
                .param("supplier", String.valueOf(medicine.getSupplier().getId()))
                .param("manufacturer", medicine.getManufacturer())
                .param("expiryDate", medicine.getExpiryDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicines"))
                .andExpect(flash().attribute("message", "Medicine updated successfully"));

        // Check if the medicine is updated in the database
        Medicine updatedMedicine = medicineRepository.findById(medicine.getId()).orElse(null);

        // Check if the medicine has the correct values
        assertAll("medicine",
                () -> assertNotNull(updatedMedicine, "Medicine was not updated successfully"),
                () -> assertEquals(medicine.getName(), updatedMedicine.getName(), "Medicine name is incorrect"),
                () -> assertEquals(medicine.getQuantity(), updatedMedicine.getQuantity(),
                        "Medicine quantity is incorrect"),
                () -> assertEquals(medicine.getPrice(), updatedMedicine.getPrice(), "Medicine price is incorrect"),
                () -> assertEquals(medicine.getSupplier(), updatedMedicine.getSupplier(),
                        "Medicine supplier is incorrect"),
                () -> assertEquals(medicine.getManufacturer(), updatedMedicine.getManufacturer(),
                        "Medicine manufacturer is incorrect"),
                () -> assertEquals(medicine.getExpiryDate(), updatedMedicine.getExpiryDate(),
                        "Medicine expiry date is incorrect"));
    }

    /*
     * Test the controller to update an medicine and expect an error when the
     * service throws an exception for duplicate medicine
     */
    @Test
    void testUpdateMedicine_Failure_WhenMedicineDoesNotExist() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create the medicine object with all required parameters
        Medicine medicine = new Medicine("Test Medicine", 100, 10.0, supplier, "Test Manufacturer", LocalDate.now());
        medicineService.createMedicine(medicine);

        // Update the medicine object with new values
        medicine.setName("Updated Equipment");
        medicine.setQuantity(20);
        medicine.setPrice(299.99);
        medicine.setManufacturer("Updated Manufacturer");
        medicine.setExpiryDate(LocalDate.now().plusDays(100));

        // Delete the medicine object to fail update due to the medicine not existing
        medicineRepository.delete(medicine);

        // Perform the POST request, simulating form submission with parameters
        mockMvc.perform(MockMvcRequestBuilders.post("/medicines/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", String.valueOf(medicine.getId()))
                .param("name", medicine.getName())
                .param("quantity", String.valueOf(medicine.getQuantity()))
                .param("price", String.valueOf(medicine.getPrice()))
                .param("supplier", String.valueOf(medicine.getSupplier().getId()))
                .param("manufacturer", medicine.getManufacturer())
                .param("expiryDate", medicine.getExpiryDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicines"))
                .andExpect(flash().attribute("errorMessage", "Error updating medicine: The medicine does not exist"));
    }

    /*
     * Test the controller to update an medicine and expect an error when the
     * service throws an exception for a negative price
     */
    @Test
    void testUpdateEquipment_Failure_WhenPriceIsNegative() {
        // Create a valid supplier first
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create a valid medicine first
        Medicine medicine = new Medicine("Test Medicine", 100, 10.0, supplier, "Test Manufacturer", LocalDate.now());
        medicineService.createMedicine(medicine);

        // Update the medicine object with valid fields except price which is negative
        medicine.setName("Updated Equipment");
        medicine.setQuantity(20);
        medicine.setPrice(-100.99);
        medicine.setManufacturer("Updated Manufacturer");
        medicine.setExpiryDate(LocalDate.now().plusDays(100));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicineService.updateMedicine(medicine);
        });
        assertEquals("The price cannot be negative", exception.getMessage());
    }

    /*
     * Test the controller to update an medicine and expect an error when the
     * service throws an exception for a negative quantity
     */
    @Test
    void testUpdateEquipment_Failure_WhenQuantityIsNegative() {
        // Create a valid supplier first
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create a valid equipment first
        Medicine medicine = new Medicine("Test Medicine", 100, 10.0, supplier, "Test Manufacturer", LocalDate.now());
        medicineService.createMedicine(medicine);

        // Update the equipment object with valid fields except negative quantity
        medicine.setName("Updated Equipment");
        medicine.setQuantity(-20);
        medicine.setPrice(299.99);
        medicine.setManufacturer("Updated Manufacturer");
        medicine.setExpiryDate(LocalDate.now().plusDays(100));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicineService.updateMedicine(medicine);
        });

        assertEquals("The quantity cannot be negative", exception.getMessage());
    }

    /*
     * Test the controller to update an medicine and expect an error when the
     * service throws an exception for a purchase date in the past
     */
    @Test
    void testUpdateMedicine_Failure_WhenPurchaseDateIsInPast() {
        // Create a valid supplier first
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create a valid medicine first
        Medicine medicine = new Medicine("Test Medicine", 100, 10.0, supplier, "Test Manufacturer", LocalDate.now());
        medicineService.createMedicine(medicine);

        medicine.setPrice(199.99);
        medicine.setQuantity(10);
        medicine.setExpiryDate(LocalDate.of(2020, 10, 10)); // Set an expiry date in the past
        medicine.setSupplier(new Supplier("Test Supplier", "123456"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicineService.updateMedicine(medicine);
        });
        assertEquals("The expiry date cannot be in the past", exception.getMessage());
    }

    /*
     * Test the controller to update an medicine and expect an error when the
     * service throws an exception for a null supplier
     */
    @Test
    void testUpdateMedicine_Failure_WhenSupplierIsNull() {
        // Create a valid supplier first
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create a valid medicine first
        Medicine medicine = new Medicine("Test Medicine", 100, 10.0, supplier, "Test Manufacturer", LocalDate.now());
        medicineService.createMedicine(medicine);

        // Update the equipment object with valid fields except supplier which is null
        medicine.setPrice(199.99);
        medicine.setQuantity(10);
        medicine.setExpiryDate(LocalDate.now());
        medicine.setSupplier(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicineService.updateMedicine(medicine);
        });
        assertEquals("The supplier does not exist", exception.getMessage());
    }

    // Test the controller to delete a medicine by ID
    @Test
    void testDeleteMedicine_Success() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create the medicine object with all required parameters
        Medicine medicine = new Medicine("Test Medicine", 100, 10.0, supplier, "Test Manufacturer", LocalDate.now());
        medicineService.createMedicine(medicine);

        // Perform the GET request to delete the equipment
        mockMvc.perform(MockMvcRequestBuilders.get("/medicines/delete/" + medicine.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicines"))
                .andExpect(flash().attribute("message", "Medicine deleted successfully"));

        // Check if the medicine was deleted from the database
        Medicine deletedEquipment = medicineRepository.findById(medicine.getId()).orElse(null);
        assertNull(deletedEquipment, "Medicine was not deleted successfully");
    }

    /*
     * Test the controller to delete a medicine by ID when the medicine does not
     * exist
     */
    @Test
    void testDeleteMedicine_Failure_WhenMedicineDoesNotExist() throws Exception {
        int nonExistentId = 99999;

        mockMvc.perform(MockMvcRequestBuilders.get("/medicines/delete/" + nonExistentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicines"))
                .andExpect(flash().attribute("errorMessage", "Error deleting medicine with ID: " + nonExistentId));
    }
}
