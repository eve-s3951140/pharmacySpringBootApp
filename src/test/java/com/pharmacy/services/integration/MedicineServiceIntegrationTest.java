package com.pharmacy.services.integration;

import java.util.Arrays;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Collection;

import static org.mockito.Mockito.*;

import com.pharmacy.models.Supplier;
import com.pharmacy.models.Medicine;
import com.pharmacy.services.MedicineService;
import com.pharmacy.repositories.MedicineRepository;

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
class MedicineServiceIntegrationTest {
    @Autowired
    private MedicineService medicineService;

    @MockBean
    private MedicineRepository medicineRepository;

    private Medicine medicine;

    @BeforeEach
    void setUp() {
        // Initialise the supplier for tests
        Supplier supplier = new Supplier();
        supplier.setId(1);
        supplier.setName("Supplier A");
        supplier.setContact("0412345678");

        // Initialise a sample medicine for tests
        medicine = new Medicine();
        medicine.setId(1);
        medicine.setName("Aspirin");
        medicine.setManufacturer("Manufacturer A");
        medicine.setSupplier(supplier);
        medicine.setPrice(10.0);
        medicine.setQuantity(100);
        medicine.setExpiryDate(LocalDate.of(2025, 12, 31));
    }

    // Test case for getting all medicines
    @Test
    void testGetAllMedicines() {
        when(medicineRepository.findAll()).thenReturn(Arrays.asList(medicine));

        Collection<Medicine> medicines = medicineService.getAllMedicines();
        assertFalse(medicines.isEmpty());
        assertEquals(1, medicines.size());
    }

    // Test case for creating a medicine
    @Test
    void testCreateMedicine() {
        when(medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(
                medicine.getName().trim(),
                medicine.getManufacturer().trim(),
                medicine.getSupplier(),
                medicine.getExpiryDate()))
                .thenReturn(null);
        when(medicineRepository.save(medicine)).thenReturn(medicine);

        assertDoesNotThrow(() -> medicineService.createMedicine(medicine));
        verify(medicineRepository, times(1)).save(medicine);
    }

    // Test case for creating a duplicate medicine
    @Test
    void testCreateDuplicateMedicine() {
        when(medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(
                medicine.getName().trim(),
                medicine.getManufacturer().trim(),
                medicine.getSupplier(),
                medicine.getExpiryDate()))
                .thenReturn(medicine);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicineService.createMedicine(medicine));
        assertEquals("The medicine with the same name, supplier, expiry date, and manufacturer already exists",
                exception.getMessage());
    }

    // Test case for updating a medicine
    @Test
    void testUpdateMedicine() {
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.of(medicine));
        when(medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(
                medicine.getName().trim(),
                medicine.getManufacturer().trim(),
                medicine.getSupplier(),
                medicine.getExpiryDate()))
                .thenReturn(null);

        assertDoesNotThrow(() -> medicineService.updateMedicine(medicine));
        verify(medicineRepository, times(1)).save(medicine);
    }

    // Test case for updating a non-existing medicine
    @Test
    void testUpdateNonExistingMedicine() {
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicineService.updateMedicine(medicine));
        assertEquals("The medicine does not exist", exception.getMessage());
    }

    // Test case for updating a medicine with a duplicate entry
    @Test
    void testUpdateDuplicateMedicine() {
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.of(medicine));
        when(medicineRepository.findByNameAndManufacturerAndSupplierAndExpiryDate(
                medicine.getName().trim(),
                medicine.getManufacturer().trim(),
                medicine.getSupplier(),
                medicine.getExpiryDate()))
                .thenReturn(new Medicine()); // Simulate a duplicate medicine

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicineService.updateMedicine(medicine));
        assertEquals("The medicine with the same name, manufacturer, supplier, and expiry date already exists",
                exception.getMessage());
    }

    // Test case for deleting a medicine
    @Test
    void testDeleteMedicine() {
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.of(medicine));

        assertDoesNotThrow(() -> medicineService.deleteMedicine(medicine.getId()));
        verify(medicineRepository, times(1)).deleteById(medicine.getId());
    }

    // Test case for deleting a non-existing medicine
    @Test
    void testDeleteNonExistingMedicine() {
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicineService.deleteMedicine(medicine.getId()));
        assertEquals("The medicine does not exist", exception.getMessage());
    }

    // Test case for validating medicine fields
    @Test
    void testCheckMedicineFields_NullSupplier() {
        medicine.setSupplier(null);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicineService.createMedicine(medicine));
        assertEquals("The supplier does not exist", exception.getMessage());
    }

    @Test
    void testCheckMedicineFields_NegativePrice() {
        medicine.setPrice(-1.0);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicineService.createMedicine(medicine));
        assertEquals("The price cannot be negative", exception.getMessage());
    }

    @Test
    void testCheckMedicineFields_NegativeQuantity() {
        medicine.setQuantity(-1);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicineService.createMedicine(medicine));
        assertEquals("The quantity cannot be negative", exception.getMessage());
    }

    @Test
    void testCheckMedicineFields_ExpiredDate() {
        medicine.setExpiryDate(LocalDate.of(2020, 1, 1));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicineService.createMedicine(medicine));
        assertEquals("The expiry date cannot be in the past", exception.getMessage());
    }
}
