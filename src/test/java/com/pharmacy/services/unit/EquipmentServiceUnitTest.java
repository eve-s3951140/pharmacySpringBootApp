package com.pharmacy.services.unit;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.boot.test.context.SpringBootTest;

import com.pharmacy.models.Equipment;
import com.pharmacy.models.Supplier;
import com.pharmacy.repositories.EquipmentRepository;
import com.pharmacy.services.servicesImplementation.EquipmentServiceImpl;

@SpringBootTest
class EquipmentServiceUnitTest {
    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    private Equipment testEquipment;

    @BeforeEach
    void setUp() {
        Supplier supplier = new Supplier();
        supplier.setId(1);
        supplier.setName("SupplierX");
        supplier.setContact("0412345678");

        testEquipment = new Equipment();
        testEquipment.setId(1); // Set the ID to 1
        testEquipment.setName("Test Equipment");
        testEquipment.setSupplier(supplier);
        testEquipment.setPrice(10.0);
        testEquipment.setQuantity(100);
        testEquipment.setPurchaseDate(LocalDate.now().minusDays(10));
        testEquipment.setWarranty("1 year");
    }

    // Test getAllEquipments
    @Test
    void testGetAllEquipments() {
        // Prepare mock data
        when(equipmentRepository.findAll()).thenReturn(Arrays.asList(testEquipment));

        Collection<Equipment> equipments = equipmentService.getAllEquipments();

        // Convert collection to list
        List<Equipment> equipmentList = new ArrayList<>(equipments);

        // Verify results
        assertNotNull(equipmentList);
        assertEquals(1, equipmentList.size());
        assertEquals("Test Equipment", equipmentList.get(0).getName());
    }

    // Test createEquipment with valid data
    @Test
    void testCreateEquipment_Success() {
        when(equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(anyString(), any(), any(),
                anyString()))
                .thenReturn(null); // No duplicate

        // Act
        equipmentService.createEquipment(testEquipment);

        // Assert that the repository's save method was called
        verify(equipmentRepository, times(1)).save(testEquipment);
    }

    // Test createEquipment with duplicate equipment
    @Test
    void testCreateEquipment_DuplicateEquipment() {
        // Simulate an existing duplicate equipment
        when(equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(anyString(), any(), any(),
                anyString()))
                .thenReturn(testEquipment);

        // Assert that the exception is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> equipmentService.createEquipment(testEquipment));

        // Assert the exception message
        assertEquals("The equipment with the same name, supplier, purchase date, and warranty already exists",
                thrown.getMessage());
    }

    // Test createEquipment with invalid purchase date
    @Test
    void testCreateEquipment_InvalidPurchaseDate() {
        // Set a future date for the test equipment's purchase date
        testEquipment.setPurchaseDate(LocalDate.now().plusDays(1));

        // Assert that the exception is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> equipmentService.createEquipment(testEquipment));

        // Assert the exception message
        assertEquals("The purchase date cannot be in the future", thrown.getMessage());
    }

    // Test updateEquipment with existing equipment
    @Test
    void testUpdateEquipment_Success() {
        // Mock existing equipment
        when(equipmentRepository.findById(1)).thenReturn(Optional.of(testEquipment));
        when(equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(anyString(), any(), any(),
                anyString()))
                .thenReturn(null); // No duplicate

        // Act
        equipmentService.updateEquipment(testEquipment);

        // Assert that the repository's save method was called
        verify(equipmentRepository, times(1)).save(testEquipment);
    }

    // Test updateEquipment with non-existent equipment
    @Test
    void testUpdateEquipment_EquipmentDoesNotExist() {
        // Simulate equipment not found in the repository
        when(equipmentRepository.findById(1)).thenReturn(Optional.empty());

        // Assert that the exception is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> equipmentService.updateEquipment(testEquipment));

        // Assert the exception message
        assertEquals("The equipment does not exist", thrown.getMessage());
    }

    // Test updateEquipment with duplicate equipment
    @Test
    void testUpdateEquipment_DuplicateEquipment() {
        // Simulate an existing duplicate equipment with a different ID
        Equipment duplicateEquipment = new Equipment();
        duplicateEquipment.setId(2); // Different ID
        when(equipmentRepository.findById(1)).thenReturn(Optional.of(testEquipment));
        when(equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(anyString(), any(), any(),
                anyString()))
                .thenReturn(duplicateEquipment); // Duplicate with a different ID

        // Assert that the exception is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> equipmentService.updateEquipment(testEquipment));

        // Assert the exception message
        assertEquals("The equipment with the same name, supplier, purchase date, and warranty already exists",
                thrown.getMessage());
    }

    // Test updateEquipment with invalid purchase date
    @Test
    void testUpdateEquipment_InvalidPurchaseDate() {
        // Mock existing equipment
        when(equipmentRepository.findById(1)).thenReturn(Optional.of(testEquipment));

        // Set a future date for the test equipment's purchase date
        testEquipment.setPurchaseDate(LocalDate.now().plusDays(1));

        // Assert that the exception is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> equipmentService.updateEquipment(testEquipment));

        // Assert the exception message
        assertEquals("The purchase date cannot be in the future", thrown.getMessage());
    }

    // Test updateEquipment with invalid ID
    @Test
    void testUpdateEquipment_InvalidID() {
        // Set an invalid ID for the test equipment
        testEquipment.setId(0);

        // Assert that the exception is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> equipmentService.updateEquipment(testEquipment));

        // Assert the exception message
        assertEquals("The equipment does not exist", thrown.getMessage());
    }

    // Test updateEquipment with negative quantity
    @Test
    void testUpdateEquipment_NegativeQuantity() {
        // Mock existing equipment
        when(equipmentRepository.findById(1)).thenReturn(Optional.of(testEquipment));

        // Set a negative quantity for the test equipment
        testEquipment.setQuantity(-1);

        // Assert that the exception is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> equipmentService.updateEquipment(testEquipment));

        // Assert the exception message
        assertEquals("The quantity cannot be negative", thrown.getMessage());
    }

    // Test updateEquipment with negative price
    @Test
    void testUpdateEquipment_NegativePrice() {
        // Mock existing equipment
        when(equipmentRepository.findById(1)).thenReturn(Optional.of(testEquipment));
        
        // Set a negative price for the test equipment
        testEquipment.setPrice(-1.0);

        // Assert that the exception is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> equipmentService.updateEquipment(testEquipment));

        // Assert the exception message
        assertEquals("The price cannot be negative", thrown.getMessage());
    }

    // Test updateEquipment with null supplier
    @Test
    void testUpdateEquipment_NullSupplier() {
        // Mock existing equipment
        when(equipmentRepository.findById(1)).thenReturn(Optional.of(testEquipment));

        // Set a null supplier for the test equipment
        testEquipment.setSupplier(null);

        // Assert that the exception is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> equipmentService.updateEquipment(testEquipment));

        // Assert the exception message
        assertEquals("The supplier does not exist", thrown.getMessage());
    }

    // Test deleteEquipment with existing equipment
    @Test
    void testDeleteEquipment_Success() {
        // Mock existing equipment
        when(equipmentRepository.findById(1)).thenReturn(Optional.of(testEquipment));

        // Act
        equipmentService.deleteEquipment(1);

        // Assert that the repository's delete method was called
        verify(equipmentRepository, times(1)).deleteById(1);
    }

    // Test deleteEquipment with non-existent equipment
    @Test
    void testDeleteEquipment_EquipmentDoesNotExist() {
        // Simulate equipment not found in the repository
        when(equipmentRepository.findById(1)).thenReturn(Optional.empty());

        // Assert that the exception is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> equipmentService.deleteEquipment(1));

        // Assert the exception message
        assertEquals("The equipment does not exist", thrown.getMessage());
    }
}