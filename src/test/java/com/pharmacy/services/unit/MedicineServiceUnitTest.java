package com.pharmacy.services.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.boot.test.context.SpringBootTest;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Arrays;
import java.util.Optional;
import java.time.LocalDate;

import com.pharmacy.models.Medicine;
import com.pharmacy.models.Supplier;
import com.pharmacy.repositories.MedicineRepository;
import com.pharmacy.services.servicesImplementation.MedicineServiceImpl;

@SpringBootTest
class MedicineServiceUnitTest {
    @Mock   
    private MedicineRepository medicineRepository;

    @InjectMocks
    private MedicineServiceImpl medicineService;

    private Medicine testMedicine;

    @BeforeEach
    void setUp() {
        Supplier supplier = new Supplier();
        supplier.setId(1);
        supplier.setName("SupplierX");
        supplier.setContact("0412345678");
    
        testMedicine = new Medicine();
        testMedicine.setId(1);  // Set the ID to 1
        testMedicine.setName("Test Medicine");
        testMedicine.setManufacturer("PharmaCo");
        testMedicine.setSupplier(supplier);
        testMedicine.setPrice(10.0);
        testMedicine.setQuantity(100);
        testMedicine.setExpiryDate(LocalDate.now().plusDays(10));
    }
    // Test getAllMedicines
    @Test
    void testGetAllMedicines() {
        // Prepare mock data
        when(medicineRepository.findAll()).thenReturn(Arrays.asList(testMedicine));

        // Call service
        var medicines = medicineService.getAllMedicines();

        // Verify results
        assertNotNull(medicines);
        assertEquals(1, medicines.size());
        assertEquals("Test Medicine", medicines.iterator().next().getName());
    }

    // Test createMedicine with valid data
    @Test
    void testCreateMedicine_Success() {
        when(medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(anyString(), anyString(), any(), any()))
                .thenReturn(null); // No duplicate

        // Act
        medicineService.createMedicine(testMedicine);

        // Assert that the repository's save method was called
        verify(medicineRepository, times(1)).save(testMedicine);
    }

    // Test createMedicine with a duplicate medicine
    @Test
    void testCreateMedicine_DuplicateMedicine() {
        // Simulate an existing duplicate medicine
        when(medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(anyString(), anyString(), any(), any()))
                .thenReturn(testMedicine);

        // Assert that the exception is thrown
        assertThrows(RuntimeException.class, () -> medicineService.createMedicine(testMedicine));
    }

    // Test createMedicine with invalid expiry date
    @Test
    void testCreateMedicine_InvalidExpiryDate() {
        // Set an expired date for the test medicine
        testMedicine.setExpiryDate(LocalDate.now().minusDays(1));

        // Assert that the exception is thrown
        assertThrows(RuntimeException.class, () -> medicineService.createMedicine(testMedicine));
    }

    // Test updateMedicine with existing medicine
    @Test
    void testUpdateMedicine_Success() {
        // Mock existing medicine
        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(anyString(), anyString(), any(), any()))
                .thenReturn(null); // No duplicate

        // Act
        medicineService.updateMedicine(testMedicine);

        // Assert that the repository's save method was called
        verify(medicineRepository, times(1)).save(testMedicine);
    }

    // Test updateMedicine with non-existent medicine
    @Test
    void testUpdateMedicine_MedicineDoesNotExist() {
        // Simulate medicine not found in the repository
        when(medicineRepository.findById(1)).thenReturn(Optional.empty());

        // Assert that the exception is thrown
        assertThrows(RuntimeException.class, () -> medicineService.updateMedicine(testMedicine));
    }

    // Test updateMedicine with duplicate medicine
    @Test
    void testUpdateMedicine_DuplicateMedicine() {
        // Simulate an existing duplicate medicine with a different ID
        Medicine duplicateMedicine = new Medicine();
        duplicateMedicine.setId(2); // Different ID
        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(anyString(), anyString(), any(), any()))
                .thenReturn(duplicateMedicine); // Duplicate with a different ID

        // Assert that the exception is thrown
        assertThrows(RuntimeException.class, () -> medicineService.updateMedicine(testMedicine));
    }

    // Test deleteMedicine with existing medicine
    @Test
    void testDeleteMedicine_Success() {
        // Mock existing medicine
        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));

        // Act
        medicineService.deleteMedicine(1);

        // Assert that the repository's delete method was called
        verify(medicineRepository, times(1)).deleteById(1);
    }

    // Test deleteMedicine with non-existent medicine
    @Test
    void testDeleteMedicine_MedicineDoesNotExist() {
        // Simulate medicine not found in the repository
        when(medicineRepository.findById(1)).thenReturn(Optional.empty());

        // Assert that the exception is thrown
        assertThrows(RuntimeException.class, () -> medicineService.deleteMedicine(1));
    }
}