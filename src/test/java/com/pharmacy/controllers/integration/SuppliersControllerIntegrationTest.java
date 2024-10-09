package com.pharmacy.controllers.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import com.pharmacy.DatabaseCleaner;
import com.pharmacy.models.Supplier;
import com.pharmacy.repositories.SupplierRepository;

@Rollback
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SuppliersControllerIntegrationTest {
        @Autowired
        MockMvc mockMvc;

        @Autowired
        private SupplierRepository supplierRepository;

        @Autowired
        private DatabaseCleaner databaseCleaner;

        // Reset the database to initial values after all tests are done
        @AfterAll
        void tearDownAll() {
                databaseCleaner.clean();
        }

        // Test adding a new supplier with valid parameters
        @Test
        void testAddSupplier_Success() throws Exception {
                // Add a new supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                // Check if the supplier is added
                assert (supplierRepository.findByName("Supplier 1") != null);

                // Check if the supplier's phone number is valid
                assert (supplierRepository.findByContact("0412345678") != null);
        }

        /**
         * Test adding a new supplier that already has the same name as another supplier
         * but with different phone number
         * 
         * @throws Exception
         */
        @Test
        void testAddSupplier_Failure_WhenSameName() throws Exception {
                // Add a new supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                // Add another supplier with the same name
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", "0498756421"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error adding supplier: Name already used by another supplier"));

                // Check if the first supplier is added
                assert (supplierRepository.findByContact("0412345678") != null);

                // Check if the first supplier has the correct name and phone number
                assert (supplierRepository.findByContact("0412345678").getName().equals("Supplier 1"));

                // Check if the second supplier is not added
                assert (supplierRepository.findByContact("0498756421") == null);
        }

        /**
         * Test adding a new supplier that already has the same phone number as another
         * supplier but with different name
         * 
         * @throws Exception
         */
        @Test
        void testAddSupplier_Failure_WhenSameContact() throws Exception {
                // Add a new supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                // Add another supplier with the same phone number
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 2000")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error adding supplier: Phone number already used by another supplier"));

                // Check if the first supplier is added
                assert (supplierRepository.findByName("Supplier 1") != null);

                // Check if the first supplier has the correct name and phone number
                assert (supplierRepository.findByName("Supplier 1").getContact().equals("0412345678"));

                // Check if the second supplier is not added
                assert (supplierRepository.findByName("Supplier 2000") == null);
        }

        /**
         * Test adding a new supplier with an invalid phone number:
         * - does not start with 04,
         * - less than 8 digits after 04,
         * - more than 8 digits after 04,
         * - contains letters,
         * - contains special characters
         * 
         * @param contact
         * @throws Exception
         */
        @ParameterizedTest
        @ValueSource(strings = { "0312345678", "041234567890", "041234", "0412BC5D7A", "0412@5678" })
        void testAddSupplier_Failure_InvalidContact(String contact) throws Exception {
                // Add a new supplier with invalid phone number
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", contact))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error adding supplier: Invalid phone number (it must start with 04 followed by 8 digits)"));

                // Check if the supplier is not added
                assert (supplierRepository.findByName("Supplier 1") == null);
        }

        // Test updating a supplier's name and contact with valid parameters
        @Test
        void testUpdateSupplier_Success() throws Exception {
                // Add a new supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                // Update the supplier
                mockMvc.perform(put("/suppliers/update")
                                .param("id", String.valueOf(supplierRepository.findByName("Supplier 1").getId()))
                                .param("name", "Supplier 2")
                                .param("contact", "0498765432"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier updated successfully"));

                // Check if the supplier is updated
                assert (supplierRepository.findByName("Supplier 2") != null);
                assert (supplierRepository.findByName("Supplier 1") == null);

                // Check if the supplier's phone number is updated
                assert (supplierRepository.findByContact("0498765432") != null);
                assert (supplierRepository.findByContact("0412345678") == null);
        }

        /**
         * Test updating a supplier's name to a name that is already used by another
         * supplier but with different phone number
         * 
         * @throws Exception
         */
        @Test
        void testUpdateSupplier_Failure_WhenSameName() throws Exception {
                // Add a new supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                // Add another supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 2")
                                .param("contact", "0498765432"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                // Update the first supplier's name to the second supplier's name
                mockMvc.perform(put("/suppliers/update")
                                .param("id", String.valueOf(supplierRepository.findByName("Supplier 1").getId()))
                                .param("name", "Supplier 2")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error updating supplier: Name already used by another supplier"));

                // Check if the first supplier's name is not updated
                assert (supplierRepository.findByName("Supplier 1") != null);
                assert (supplierRepository.findByName("Supplier 2") != null);
        }

        /**
         * Test updating a supplier's phone number to a phone number that is already
         * used by another supplier but with different name
         * 
         * @throws Exception
         */
        @Test
        void testUpdateSupplier_Failure_WhenSameContact() throws Exception {
                // Add a new supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                // Add another supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 2")
                                .param("contact", "0498765432"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                /**
                 * Update the first supplier's phone number to the second supplier's phone
                 */
                mockMvc.perform(put("/suppliers/update")
                                .param("id", String.valueOf(supplierRepository.findByName("Supplier 1").getId()))
                                .param("name", "Supplier 1")
                                .param("contact", "0498765432"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error updating supplier: Phone number already used by another supplier"));

                // Check if the first supplier's phone number is not updated
                assert (supplierRepository.findByContact("0412345678") != null);
                assert (supplierRepository.findByContact("0498765432") != null);
        }

        /**
         * Test updating a supplier's phone number to an invalid phone number:
         * - does not start with 04,
         * - less than 8 digits after 04,
         * - more than 8 digits after 04,
         * - contains letters,
         * - contains special characters
         * 
         * @param contact
         * @throws Exception
         */
        @ParameterizedTest
        @ValueSource(strings = { "0312345678", "041234567890", "041234", "0412BC5D7A", "0412@5678" })
        void testUpdateSupplier_Failure_InvalidContact(String contact) throws Exception {
                // Add a new supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                // Update the supplier with invalid phone number
                mockMvc.perform(put("/suppliers/update")
                                .param("id", String.valueOf(supplierRepository.findByName("Supplier 1").getId()))
                                .param("name", "Supplier 1")
                                .param("contact", contact))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error updating supplier: Invalid phone number (it must start with 04 followed by 8 digits)"));

                // Check if the supplier's phone number is not updated
                assert (supplierRepository.findByContact("0412345678") != null);
                assert (supplierRepository.findByContact(contact) == null);
        }

        // Test deleting a supplier
        @Test
        void testDeleteSupplier_Success() throws Exception {
                // Add a new supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                // Delete the supplier
                mockMvc.perform(MockMvcRequestBuilders
                                .delete("/suppliers/delete/" + supplierRepository.findByName("Supplier 1").getId()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier deleted successfully"));

                // Check if the supplier is deleted
                assert (supplierRepository.findByName("Supplier 1") == null);
        }

        // Test deleting a supplier that has associated products
        @Test
        void testDeleteSupplier_Failure_WhenAssociatedProducts() throws Exception {
                // Add a new supplier
                mockMvc.perform(post("/suppliers/add")
                                .param("name", "Supplier 1")
                                .param("contact", "0412345678"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier added successfully"));

                // Get the newly added supplier's ID
                Supplier supplier = supplierRepository.findByName("Supplier 1");
                assertNotNull(supplier, "Supplier 1 should have been added to the repository");

                // Add a new product (medicine) associated with the supplier
                mockMvc.perform(MockMvcRequestBuilders.post("/medicines/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name", "Medicine 1")
                                .param("quantity", "10")
                                .param("price", "10.0")
                                .param("supplier", String.valueOf(supplier.getId()))
                                .param("manufacturer", "Manufacturer 1")
                                .param("expiryDate", "2025-12-12"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/medicines"))
                                .andExpect(flash().attribute("message", "Medicine added successfully"));

                // Attempt to delete the supplier with associated products
                mockMvc.perform(MockMvcRequestBuilders.delete("/suppliers/delete/" + supplier.getId()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage", "Error deleting supplier with ID: "
                                                + supplier.getId() + ", due to Supplier has associated products"));

                // Check that the supplier is not deleted due to associated products
                assertTrue(supplierRepository.findById(supplier.getId()).isPresent(),
                                "Supplier 1 should not be deleted");
        }

        // Test deleting a supplier that does not exist
        @Test
        void testDeleteSupplier_Failure_WhenSupplierDoesNotExist() throws Exception {
                // Perform the GET request to delete the equipment
                mockMvc.perform(MockMvcRequestBuilders.delete("/suppliers/delete/9999999"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error deleting supplier with ID: 9999999, due to Supplier does not exist"));
        }
}