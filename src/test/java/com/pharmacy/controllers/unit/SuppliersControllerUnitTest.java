package com.pharmacy.controllers.unit;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;

import com.pharmacy.models.Supplier;
import com.pharmacy.services.SupplierService;
import com.pharmacy.controllers.SuppliersController;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

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

        /**
         * Test the controller to add a new supplier and simulate an exception being
         * thrown when adding the supplier fails due to an error
         * 
         * @throws Exception
         */
        @Test
        void testAddSupplier_Failure() throws Exception {
                // Mock the createSupplier method to throw an exception when called
                doThrow(new RuntimeException("Error adding supplier")).when(supplierService)
                                .createSupplier(any(Supplier.class));

                mockMvc.perform(post("/suppliers/add")
                                .flashAttr("supplier", new Supplier()))
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error adding supplier: Error adding supplier"));

                // Verify that the createSupplier method of supplierService is called once
                verify(supplierService, times(1)).createSupplier(any(Supplier.class));
        }

        // Test the controller to update a supplier
        @Test
        void testUpdateSupplier_Success() throws Exception {
                mockMvc.perform(put("/suppliers/update")
                                .flashAttr("supplier", new Supplier()))
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier updated successfully"));

                // Verify that the updateSupplier method is called only once
                verify(supplierService, times(1)).updateSupplier(any(Supplier.class));
        }

        /**
         * Test the controller to update a supplier and simulate an exception being
         * thrown when updating the supplier fails due to an error
         * 
         * @throws Exception
         */
        @Test
        void testUpdateSupplier_Failure() throws Exception {
                // Mock the updateSupplier method to throw an exception when called
                doThrow(new RuntimeException("Error updating supplier")).when(supplierService)
                                .updateSupplier(any(Supplier.class));

                mockMvc.perform(put("/suppliers/update")
                                .flashAttr("supplier", new Supplier()))
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error updating supplier: Error updating supplier"));

                // Verify that the updateSupplier method of supplierService is called once
                verify(supplierService, times(1)).updateSupplier(any(Supplier.class));
        }

        // Test the controller to delete a supplier by ID
        @Test
        void testDeleteSupplier_Success() throws Exception {
                mockMvc.perform(delete("/suppliers/delete/1"))
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("message", "Supplier deleted successfully"));

                // Verify that the deleteSupplier method is called only once
                verify(supplierService, times(1)).deleteSupplier(1);
        }

        /**
         * Test the controller to delete a supplier by ID and simulate an exception
         * being thrown when the supplier has associated products
         * 
         * @throws Exception
         */
        @Test
        void testDeleteSupplier_Failure_WhenSupplierHasAssociatedProducts() throws Exception {
                int supplierId = 1;

                /**
                 * Mock the deleteSupplier method to throw an exception indicating that the
                 * supplier has associated products
                 */
                doThrow(new RuntimeException("supplier has associated products")).when(supplierService)
                                .deleteSupplier(supplierId);

                // Perform a DELETE request to the delete supplier endpoint
                mockMvc.perform(MockMvcRequestBuilders.delete("/suppliers/delete/" + supplierId))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error deleting supplier with ID: " + supplierId
                                                                + ", due to supplier has associated products"));

                // Verify that the deleteSupplier method of supplierService is called once
                verify(supplierService, times(1)).deleteSupplier(supplierId);
        }

        /**
         * Test the controller to delete a supplier by ID and simulate an exception
         * being thrown when the supplier does not exist
         */
        @Test
        void testDeleteSupplier_Failure_WhenSupplierNotFound() throws Exception {
                int supplierId = 1;

                /**
                 * Mock the deleteSupplier method to throw an exception indicating that the
                 * supplier does not exist
                 */
                doThrow(new RuntimeException("supplier does not exist")).when(supplierService)
                                .deleteSupplier(supplierId);

                // Perform a DELETE request to the delete supplier endpoint
                mockMvc.perform(MockMvcRequestBuilders.delete("/suppliers/delete/" + supplierId))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/suppliers"))
                                .andExpect(flash().attribute("errorMessage",
                                                "Error deleting supplier with ID: " + supplierId
                                                                + ", due to supplier does not exist"));

                // Verify that the deleteSupplier method of supplierService is called once
                verify(supplierService, times(1)).deleteSupplier(supplierId);
        }
}
