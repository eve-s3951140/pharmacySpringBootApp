package com.pharmacy.services.integration;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;

import com.pharmacy.models.Medicine;
import com.pharmacy.models.Supplier;
import com.pharmacy.services.SupplierService;
import com.pharmacy.repositories.ProductRepository;
import com.pharmacy.repositories.SupplierRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.test.annotation.Rollback;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Rollback
@Transactional
@SpringBootTest
class SupplierServiceIntegrationTest {
    @Autowired
    private SupplierService supplierService;

    @MockBean
    private SupplierRepository supplierRepository;

    @MockBean
    private ProductRepository productRepository;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        // Initialize a sample supplier for tests
        supplier = new Supplier();
        supplier.setId(1);
        supplier.setName("Supplier A");
        supplier.setContact("0412345678");
    }

    // Test case for getting all suppliers
    @Test
    void testGetAllSuppliers() {
        when(supplierRepository.findAll()).thenReturn(Arrays.asList(supplier));

        Collection<Supplier> suppliers = supplierService.getAllSuppliers();

        assertEquals(1, suppliers.size());
        assertTrue(suppliers.contains(supplier));
    }

    // Test case for creating a new supplier
    @Test
    void testCreateSupplier() {
        when(supplierRepository.findByName(anyString())).thenReturn(null);
        when(supplierRepository.findByContact(anyString())).thenReturn(null);

        supplierService.createSupplier(supplier);

        verify(supplierRepository, times(1)).save(supplier);
    }

    /**
     * Test case for creating a new supplier with the same name as an existing
     * supplier
     */
    @Test
    void testCreateSupplierWithSameName() {
        Supplier existingSupplier = new Supplier();
        existingSupplier.setId(2);
        existingSupplier.setName("Supplier A");
        existingSupplier.setContact("0412345678");

        when(supplierRepository.findByName(anyString())).thenReturn(existingSupplier);

        // Assert that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierService.createSupplier(supplier);
        });

        // Assert the exception message
        assertEquals("Name already used by another supplier", exception.getMessage());

        verify(supplierRepository, times(0)).save(supplier);
    }

    /**
     * Test case for creating a new supplier with the same phone number as an
     * existing supplier
     */
    @Test
    void testCreateSupplierWithSamePhoneNumber() {
        Supplier existingSupplier = new Supplier();
        existingSupplier.setId(2);
        existingSupplier.setName("Supplier B");
        existingSupplier.setContact("0412345678");

        when(supplierRepository.findByName(anyString())).thenReturn(null);
        when(supplierRepository.findByContact(anyString())).thenReturn(existingSupplier);

        // Assert that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierService.createSupplier(supplier);
        });

        // Assert the exception message
        assertEquals("Phone number already used by another supplier", exception.getMessage());

        verify(supplierRepository, times(0)).save(supplier);
    }

    // Test case for creating a new supplier with an invalid phone number
    @Test
    void testCreateSupplierWithInvalidPhoneNumber() {
        supplier.setContact("04123456789");

        // Assert that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierService.createSupplier(supplier);
        });

        // Assert the exception message
        assertEquals("Invalid phone number (it must start with 04 followed by 8 digits)", exception.getMessage());

        verify(supplierRepository, times(0)).save(supplier);
    }

    // Test case for updating a supplier
    @Test
    void testUpdateSupplier() {
        when(supplierRepository.findById(supplier.getId())).thenReturn(java.util.Optional.of(supplier));
        when(supplierRepository.findByName(anyString())).thenReturn(null);
        when(supplierRepository.findByContact(anyString())).thenReturn(null);

        supplierService.updateSupplier(supplier);

        verify(supplierRepository, times(1)).save(supplier);
    }

    // Test case for updating a supplier with the same name as an existing supplier
    @Test
    void testUpdateSupplierWithSameName() {
        Supplier existingSupplier = new Supplier();
        existingSupplier.setId(2);
        existingSupplier.setName("Supplier A");
        existingSupplier.setContact("0412345678");

        when(supplierRepository.findById(supplier.getId())).thenReturn(java.util.Optional.of(supplier));
        when(supplierRepository.findByName(anyString())).thenReturn(existingSupplier);

        // Assert that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierService.updateSupplier(supplier);
        });

        // Assert the exception message
        assertEquals("Name already used by another supplier", exception.getMessage());

        verify(supplierRepository, times(0)).save(supplier);
    }

    /**
     * Test case for updating a supplier with the same phone number as an existing
     * supplier
     */
    @Test
    void testUpdateSupplierWithSamePhoneNumber() {
        Supplier existingSupplier = new Supplier();
        existingSupplier.setId(2);
        existingSupplier.setName("Supplier B");
        existingSupplier.setContact("0412345678");

        when(supplierRepository.findById(supplier.getId())).thenReturn(java.util.Optional.of(supplier));
        when(supplierRepository.findByContact(anyString())).thenReturn(existingSupplier);

        // Assert that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierService.updateSupplier(supplier);
        });

        // Assert the exception message
        assertEquals("Phone number already used by another supplier", exception.getMessage());

        verify(supplierRepository, times(0)).save(supplier);
    }

    // Test case for updating a supplier with an invalid phone number
    @Test
    void testUpdateSupplierWithInvalidPhoneNumber() {
        // Setting an invalid phone number
        supplier.setContact("04123456789");

        // Mocking the behavior of the repository to return the existing supplier
        when(supplierRepository.findById(supplier.getId())).thenReturn(java.util.Optional.of(supplier));

        // Assert that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierService.updateSupplier(supplier);
        });

        // Assert the exception message
        assertEquals("Invalid phone number (it must start with 04 followed by 8 digits)", exception.getMessage());

        // Verify that save was never called
        verify(supplierRepository, times(0)).save(supplier);
    }

    // Test case for updating a non-existing supplier
    @Test
    void testUpdateNonExistingSupplier() {
        when(supplierRepository.findById(supplier.getId())).thenReturn(java.util.Optional.empty());

        // Assert that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierService.updateSupplier(supplier);
        });

        // Assert the exception message
        assertEquals("Supplier does not exist", exception.getMessage());

        verify(supplierRepository, times(0)).save(supplier);
    }

    // Test case for deleting a supplier
    @Test
    void testDeleteSupplier() {
        when(supplierRepository.findById(supplier.getId())).thenReturn(java.util.Optional.of(supplier));

        supplierService.deleteSupplier(supplier.getId());

        verify(supplierRepository, times(1)).deleteById(supplier.getId());
    }

    // Test case for deleting a non-existing supplier
    @Test
    void testDeleteNonExistingSupplier() {
        when(supplierRepository.findById(supplier.getId())).thenReturn(java.util.Optional.empty());

        // Assert that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierService.deleteSupplier(supplier.getId());
        });

        // Assert the exception message
        assertEquals("Supplier does not exist", exception.getMessage());

        verify(supplierRepository, times(0)).deleteById(supplier.getId());
    }

    // Test case for deleting a supplier with associated products
    @Test
    void testDeleteSupplierWithAssociatedProducts() {
        // Create a mock Medicine object
        Medicine mockMedicine = new Medicine();
        mockMedicine.setId(1); // Set any necessary properties for the mock

        // Set up the supplier repository to return the supplier when looked up by ID
        when(supplierRepository.findById(supplier.getId())).thenReturn(java.util.Optional.of(supplier));

        // Mock to simulate that the supplier has associated medicines
        when(productRepository.findBySupplierId(supplier.getId())).thenReturn(Arrays.asList(mockMedicine));

        // Assert that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierService.deleteSupplier(supplier.getId());
        });

        // Assert the exception message
        assertEquals("Supplier has associated products", exception.getMessage());

        // Verify that deleteById was never called
        verify(supplierRepository, times(0)).deleteById(supplier.getId());
    }
}
