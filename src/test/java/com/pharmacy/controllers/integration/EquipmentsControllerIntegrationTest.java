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
import com.pharmacy.models.Supplier;
import com.pharmacy.models.Equipment;
import com.pharmacy.services.SupplierService;
import com.pharmacy.services.EquipmentService;
import com.pharmacy.repositories.EquipmentRepository;

@Rollback
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @AfterAll
class EquipmentsControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    // Clean the database after all tests are done (reset the auto-increment values)
    @AfterAll
    void tearDownAll() {
        databaseCleaner.clean();
    }

    // Test the controller to add a new equipment
    @Test
    void testAddEquipment_Success() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create the equipment object with all required parameters
        Equipment equipment = new Equipment("1 year", LocalDate.now(), "Test Equipment", 10, 199.99, supplier);

        // Perform the POST request, simulating form submission with parameters
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", equipment.getName())
                .param("quantity", String.valueOf(equipment.getQuantity()))
                .param("price", String.valueOf(equipment.getPrice()))
                .param("warranty", equipment.getWarranty())
                .param("purchaseDate", equipment.getPurchaseDate().toString())
                .param("supplier", String.valueOf(equipment.getSupplier().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("message", "Equipment added successfully"));

        // Check if the equipment was saved in the database
        Equipment savedEquipment = equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(
                equipment.getName(), equipment.getSupplier(), equipment.getPurchaseDate(), equipment.getWarranty());

        /*
         * Check if the saved equipment is not null and has the same attributes as the
         * equipment object
         */
        assertAll("equipment",
                () -> assertNotNull(savedEquipment, "Equipment was not saved"),
                () -> assertEquals(equipment.getName(), savedEquipment.getName(), "Name is not equal"),
                () -> assertEquals(equipment.getQuantity(), savedEquipment.getQuantity(), "Quantity is not equal"),
                () -> assertEquals(equipment.getPrice(), savedEquipment.getPrice(), "Price is not equal"),
                () -> assertEquals(equipment.getWarranty(), savedEquipment.getWarranty(), "Warranty is not equal"),
                () -> assertEquals(equipment.getPurchaseDate(), savedEquipment.getPurchaseDate(),
                        "Purchase date is not equal"),
                () -> assertEquals(equipment.getSupplier(), savedEquipment.getSupplier(), "Supplier is not equal"),
                () -> assertEquals(equipment.getSupplier().getId(), savedEquipment.getSupplier().getId(),
                        "Supplier ID is not equal"));
    }

    /*
     * Test the controller to add a new equipment and expect an error when the
     * service throws an exception for duplicate equipment
     */
    @Test
    void testAddEquipment_Failure_WhenDuplicateEquipment() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create the equipment object with all required parameters
        Equipment equipment = new Equipment("1 year", LocalDate.now(), "Test Equipment", 10, 199.99, supplier);

        // Mock the createEquipment method to throw a RuntimeException when called
        equipmentService.createEquipment(equipment);

        // Perform the POST request, simulating form submission with parameters
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", equipment.getName())
                .param("quantity", String.valueOf(equipment.getQuantity()))
                .param("price", String.valueOf(equipment.getPrice()))
                .param("warranty", equipment.getWarranty())
                .param("purchaseDate", equipment.getPurchaseDate().toString())
                .param("supplier", String.valueOf(equipment.getSupplier().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("errorMessage",
                        "Error adding equipment: The equipment with the same name, supplier, purchase date, and warranty already exists"));
    }

    /*
     * Test the controller to add a new equipment and expect an error when the
     * service throws an exception for a negative price
     */
    @Test
    void testAddEquipment_Failure_WhenPriceIsNegative() {
        Equipment equipment = new Equipment();
        equipment.setPrice(-1.0); // Set a negative price
        equipment.setQuantity(10);
        equipment.setPurchaseDate(LocalDate.now());
        equipment.setSupplier(new Supplier("Test Supplier", "123456"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            equipmentService.createEquipment(equipment);
        });
        assertEquals("The price cannot be negative", exception.getMessage());
    }

    /*
     * Test the controller to add a new equipment and expect an error when the
     * service throws an exception for a negative quantity
     */
    @Test
    void testCreateEquipment_Failure_WhenQuantityIsNegative() {
        Equipment equipment = new Equipment();
        equipment.setPrice(199.99);
        equipment.setQuantity(-1); // Set a negative quantity
        equipment.setPurchaseDate(LocalDate.now());
        equipment.setSupplier(new Supplier("Test Supplier", "123456"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            equipmentService.createEquipment(equipment);
        });

        assertEquals("The quantity cannot be negative", exception.getMessage());
    }

    /*
     * Test the controller to add a new equipment and expect an error when the
     * service throws an exception for a purchase date in the future
     */
    @Test
    void testCreateEquipment_Failure_WhenPurchaseDateIsInFuture() {
        Equipment equipment = new Equipment();
        equipment.setPrice(199.99);
        equipment.setQuantity(10);
        equipment.setPurchaseDate(LocalDate.now().plusDays(1)); // Set a future purchase date
        equipment.setSupplier(new Supplier("Test Supplier", "123456"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            equipmentService.createEquipment(equipment);
        });
        assertEquals("The purchase date cannot be in the future", exception.getMessage());
    }

    /*
     * Test the controller to add a new equipment and expect an error when the
     * service throws an exception for a null supplier
     */
    @Test
    void testCreateEquipment_Failure_WhenSupplierIsNull() {
        Equipment equipment = new Equipment();
        equipment.setPrice(199.99);
        equipment.setQuantity(10);
        equipment.setPurchaseDate(LocalDate.now());
        equipment.setSupplier(null); // Set supplier to null

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            equipmentService.createEquipment(equipment);
        });
        assertEquals("The supplier does not exist", exception.getMessage());
    }

    // Test the controller to update an equipment
    @Test
    void testUpdateEquipment_Success() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create the equipment object with all required parameters
        Equipment equipment = new Equipment("1 year", LocalDate.now(), "Test Equipment", 10, 199.99, supplier);
        equipmentService.createEquipment(equipment);

        // Update the equipment object with new values
        equipment.setName("Updated Equipment");
        equipment.setQuantity(20);
        equipment.setPrice(299.99);
        equipment.setWarranty("2 years");
        equipment.setPurchaseDate(LocalDate.now().minusDays(1));

        // Perform the POST request, simulating form submission with parameters
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", String.valueOf(equipment.getId()))
                .param("name", equipment.getName())
                .param("quantity", String.valueOf(equipment.getQuantity()))
                .param("price", String.valueOf(equipment.getPrice()))
                .param("warranty", equipment.getWarranty())
                .param("purchaseDate", equipment.getPurchaseDate().toString())
                .param("supplier", String.valueOf(equipment.getSupplier().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("message", "Equipment updated successfully"));

        // Check if the equipment was updated in the database
        Equipment updatedEquipment = equipmentRepository.findById(equipment.getId()).orElse(null);

        /*
         * Check if the updated equipment is not null and has the same attributes as
         * the equipment object
         */
        assertAll("equipment",
                () -> assertNotNull(updatedEquipment, "Equipment was not updated"),
                () -> assertEquals(equipment.getName(), updatedEquipment.getName(), "Name is not equal"),
                () -> assertEquals(equipment.getQuantity(), updatedEquipment.getQuantity(), "Quantity is not equal"),
                () -> assertEquals(equipment.getPrice(), updatedEquipment.getPrice(), "Price is not equal"),
                () -> assertEquals(equipment.getWarranty(), updatedEquipment.getWarranty(), "Warranty is not equal"),
                () -> assertEquals(equipment.getPurchaseDate(), updatedEquipment.getPurchaseDate(),
                        "Purchase date is not equal"),
                () -> assertEquals(equipment.getSupplier(), updatedEquipment.getSupplier(), "Supplier is not equal"),
                () -> assertEquals(equipment.getSupplier().getId(), updatedEquipment.getSupplier().getId(),
                        "Supplier ID is not equal"));
    }

    /*
     * Test the controller to update an equipment and expect an error when the
     * service throws an exception for duplicate equipment
     */
    @Test
    void testUpdateEquipment_Failure_WhenEquipmentDoesNotExist() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create the equipment object with all required parameters
        Equipment equipment = new Equipment("1 year", LocalDate.now(), "Test Equipment", 10, 199.99, supplier);
        equipmentService.createEquipment(equipment);

        // Update the equipment object with new values
        equipment.setName("Updated Equipment");
        equipment.setQuantity(20);
        equipment.setPrice(299.99);
        equipment.setWarranty("2 years");
        equipment.setPurchaseDate(LocalDate.now().minusDays(1));

        // Delete the equipment object to fail update due to the equipment not existing
        equipmentRepository.delete(equipment);

        // Perform the POST request, simulating form submission with parameters
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", String.valueOf(equipment.getId()))
                .param("name", equipment.getName())
                .param("quantity", String.valueOf(equipment.getQuantity()))
                .param("price", String.valueOf(equipment.getPrice()))
                .param("warranty", equipment.getWarranty())
                .param("purchaseDate", equipment.getPurchaseDate().toString())
                .param("supplier", String.valueOf(equipment.getSupplier().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("errorMessage", "Error updating equipment: The equipment does not exist"));
    }

    /*
     * Test the controller to update an equipment and expect an error when the
     * service throws an exception for a negative price
     */
    @Test
    void testUpdateEquipment_Failure_WhenPriceIsNegative() {
        // Create a valid supplier first
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create a valid equipment first
        Equipment equipment = new Equipment("Test Equipment", LocalDate.now(), "Test Equipment", 10, 199.99, supplier);
        equipmentRepository.save(equipment);

        // Update the equipment object with valid fields except price which is negative
        equipment.setPrice(-1.0);
        equipment.setQuantity(10);
        equipment.setPurchaseDate(LocalDate.now());
        equipment.setSupplier(new Supplier("Test Supplier", "123456"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            equipmentService.updateEquipment(equipment);
        });
        assertEquals("The price cannot be negative", exception.getMessage());
    }

    /*
     * Test the controller to update an equipment and expect an error when the
     * service throws an exception for a negative quantity
     */
    @Test
    void testUpdateEquipment_Failure_WhenQuantityIsNegative() {
        // Create a valid supplier first
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create a valid equipment first
        Equipment equipment = new Equipment("Test Equipment", LocalDate.now(), "Test Equipment", 10, 199.99, supplier);
        equipmentRepository.save(equipment);

        // Update the equipment object with valid fields except negative quantity
        equipment.setPrice(199.99);
        equipment.setQuantity(-1);
        equipment.setPurchaseDate(LocalDate.now());
        equipment.setSupplier(new Supplier("Test Supplier", "123456"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            equipmentService.updateEquipment(equipment);
        });
        assertEquals("The quantity cannot be negative", exception.getMessage());
    }

    /*
     * Test the controller to update an equipment and expect an error when the
     * service throws an exception for a purchase date in the future
     */
    @Test
    void testUpdateEquipment_Failure_WhenPurchaseDateIsInFuture() {
        // Create a valid supplier first
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create a valid equipment first
        Equipment equipment = new Equipment("Test Equipment", LocalDate.now(), "Test Equipment", 10, 199.99, supplier);
        equipmentRepository.save(equipment);

        // Update the equipment object with valid fields except purchase date which is
        // in the future
        equipment.setPrice(199.99);
        equipment.setQuantity(10);
        equipment.setPurchaseDate(LocalDate.now().plusDays(100));
        equipment.setSupplier(new Supplier("Test Supplier", "123456"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            equipmentService.updateEquipment(equipment);
        });
        assertEquals("The purchase date cannot be in the future", exception.getMessage());
    }

    /*
     * Test the controller to update an equipment and expect an error when the
     * service throws an exception for a null supplier
     */
    @Test
    void testUpdateEquipment_Failure_WhenSupplierIsNull() {
        // Create a valid supplier first
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create a valid equipment first
        Equipment equipment = new Equipment("Test Equipment", LocalDate.now(), "Test Equipment", 10, 199.99, supplier);
        equipmentRepository.save(equipment);

        // Update the equipment object with valid fields except supplier which is null
        equipment.setPrice(199.99);
        equipment.setQuantity(10);
        equipment.setPurchaseDate(LocalDate.now());
        equipment.setSupplier(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            equipmentService.updateEquipment(equipment);
        });
        assertEquals("The supplier does not exist", exception.getMessage());
    }

    // Test the controller to delete an equipment by ID
    @Test
    void testDeleteEquipment_Success() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");
        supplierService.createSupplier(supplier);

        // Create the equipment object with all required parameters
        Equipment equipment = new Equipment("1 year", LocalDate.now(), "Test Equipment", 10, 199.99, supplier);
        equipmentService.createEquipment(equipment);

        // Perform the GET request to delete the equipment
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/delete/" + equipment.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("message", "Equipment deleted successfully"));

        // Check if the equipment was deleted from the database
        Equipment deletedEquipment = equipmentRepository.findById(equipment.getId()).orElse(null);
        assertNull(deletedEquipment, "Equipment was not deleted");
    }

    /*
     * Test the controller to delete an equipment by ID when the equipment does not
     * exist
     */
    @Test
    void testDeleteEquipment_Failure_WhenEquipmentDoesNotExist() throws Exception {
        int nonExistentId = 99999;

        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/delete/" + nonExistentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("errorMessage", "Error deleting equipment with ID: " + nonExistentId));
    }
}
