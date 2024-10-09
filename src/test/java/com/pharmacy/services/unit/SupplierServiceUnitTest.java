package com.pharmacy.services.unit;

import java.util.Optional;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import org.springframework.boot.test.context.SpringBootTest;

import com.pharmacy.models.Medicine;
import com.pharmacy.models.Supplier;
import com.pharmacy.repositories.ProductRepository;
import com.pharmacy.repositories.SupplierRepository;
import com.pharmacy.services.servicesImplementation.SupplierServiceImpl;

@SpringBootTest
class SupplierServiceUnitTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier supplier;

    // Set up a sample supplier object before each test
    @BeforeEach
    public void setUp() {
        supplier = new Supplier();
        supplier.setId(1);
        supplier.setName("Chemist Pharma");
        supplier.setContact("0412345678");
    }

    // Test case for creating a new supplier
    @Test
    void testCreateSupplier_Success() {
        when(supplierRepository.findByName(supplier.getName().trim())).thenReturn(null);
        when(supplierRepository.findByContact(supplier.getContact())).thenReturn(null);

        supplierService.createSupplier(supplier);

        verify(supplierRepository, times(1)).save(supplier);
    }

    // Test case for creating a new supplier with a name that is already used
    @Test
    void testCreateSupplier_NameAlreadyUsed() {
        when(supplierRepository.findByName(supplier.getName().trim())).thenReturn(new Supplier());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supplierService.createSupplier(supplier));

        assertEquals("Name already used by another supplier", exception.getMessage());
        verify(supplierRepository, never()).save(any());
    }

    // Test case for creating a new supplier with an invalid phone number
    @Test
    void testCreateSupplier_InvalidPhoneNumber() {
        supplier.setContact("123456789"); // Invalid contact
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supplierService.createSupplier(supplier));

        assertEquals("Invalid phone number (it must start with 04 followed by 8 digits)", exception.getMessage());
        verify(supplierRepository, never()).save(any());
    }

    /**
     * Test case for creating a new supplier with a phone number that is already
     * used
     */
    @Test
    void testUpdateSupplier_Success() {
        when(supplierRepository.findById(supplier.getId())).thenReturn(Optional.of(supplier));
        when(supplierRepository.findByName(supplier.getName().trim())).thenReturn(null);
        when(supplierRepository.findByContact(supplier.getContact())).thenReturn(null);

        supplierService.updateSupplier(supplier);

        verify(supplierRepository, times(1)).save(supplier);
    }

    // Test case for updating a supplier with a name that is already used
    @Test
    void testUpdateSupplier_SupplierNotExist() {
        when(supplierRepository.findById(supplier.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supplierService.updateSupplier(supplier));

        assertEquals("Supplier does not exist", exception.getMessage());
        verify(supplierRepository, never()).save(any());
    }

    // Test case for updating a supplier with a name that is already used
    @Test
    void testDeleteSupplier_Success() {
        when(supplierRepository.findById(supplier.getId())).thenReturn(Optional.of(supplier));
        when(productRepository.findBySupplierId(supplier.getId())).thenReturn(Collections.emptyList());

        supplierService.deleteSupplier(supplier.getId());

        verify(supplierRepository, times(1)).deleteById(supplier.getId());
    }

    // Test case for deleting a supplier that does not exist
    @Test
    void testDeleteSupplier_SupplierNotExist() {
        when(supplierRepository.findById(supplier.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supplierService.deleteSupplier(supplier.getId()));

        assertEquals("Supplier does not exist", exception.getMessage());
        verify(supplierRepository, never()).deleteById(anyInt());
    }

    // Test case for deleting a supplier that has associated products
    @Test
    void testDeleteSupplier_HasAssociatedProducts() {
        when(supplierRepository.findById(supplier.getId())).thenReturn(Optional.of(supplier));
        // Now using Medicine instead of Product
        when(productRepository.findBySupplierId(supplier.getId()))
                .thenReturn(Collections.singletonList(new Medicine()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supplierService.deleteSupplier(supplier.getId()));

        assertEquals("Supplier has associated products", exception.getMessage());
        verify(supplierRepository, never()).deleteById(anyInt());
    }
}