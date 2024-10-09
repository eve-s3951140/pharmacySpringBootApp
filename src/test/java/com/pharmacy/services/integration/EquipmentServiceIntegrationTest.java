package com.pharmacy.services.integration;

import java.util.Arrays;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Collection;

import static org.mockito.Mockito.*;

import com.pharmacy.models.Supplier;
import com.pharmacy.models.Equipment;
import com.pharmacy.services.EquipmentService;
import com.pharmacy.repositories.EquipmentRepository;

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
class EquipmentServiceIntegrationTest {
    @Autowired
    private EquipmentService equipmentService;

    @MockBean
    private EquipmentRepository equipmentRepository;

    private Equipment equipment;

    @BeforeEach
    void setUp() {
        // Initialize the supplier for tests
        Supplier supplier = new Supplier();
        supplier.setId(1);
        supplier.setName("Supplier A");
        supplier.setContact("0412345678");

        // Initialize a sample equipment for tests
        equipment = new Equipment();
        equipment.setId(1);
        equipment.setName("MRI Machine");
        equipment.setSupplier(supplier);
        equipment.setPrice(10000.0);
        equipment.setQuantity(10);
        equipment.setPurchaseDate(LocalDate.of(2024, 1, 1));
        equipment.setWarranty("2 years");
    }

    // Test case for getting all equipment
    @Test
    void testGetAllEquipments() {
        when(equipmentRepository.findAll()).thenReturn(Arrays.asList(equipment));

        Collection<Equipment> equipments = equipmentService.getAllEquipments();
        assertFalse(equipments.isEmpty());
        assertEquals(1, equipments.size());
    }

    // Test case for creating equipment
    @Test
    void testCreateEquipment() {
        when(equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(
                equipment.getName().trim(),
                equipment.getSupplier(),
                equipment.getPurchaseDate(),
                equipment.getWarranty()))
                .thenReturn(null); // No duplicate
        when(equipmentRepository.save(equipment)).thenReturn(equipment);

        assertDoesNotThrow(() -> equipmentService.createEquipment(equipment));
        verify(equipmentRepository, times(1)).save(equipment);
    }

    // Test case for creating a duplicate equipment
    @Test
    void testCreateDuplicateEquipment() {
        when(equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(
                equipment.getName().trim(),
                equipment.getSupplier(),
                equipment.getPurchaseDate(),
                equipment.getWarranty()))
                .thenReturn(equipment); // Simulate existing equipment

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> equipmentService.createEquipment(equipment));
        assertEquals("The equipment with the same name, supplier, purchase date, and warranty already exists",
                exception.getMessage());
    }

    // Test case for updating equipment
    @Test
    void testUpdateEquipment() {
        when(equipmentRepository.findById(equipment.getId())).thenReturn(Optional.of(equipment));
        when(equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(
                equipment.getName().trim(),
                equipment.getSupplier(),
                equipment.getPurchaseDate(),
                equipment.getWarranty()))
                .thenReturn(null); // No duplicate

        assertDoesNotThrow(() -> equipmentService.updateEquipment(equipment));
        verify(equipmentRepository, times(1)).save(equipment);
    }

    // Test case for updating a non-existing equipment
    @Test
    void testUpdateNonExistingEquipment() {
        when(equipmentRepository.findById(equipment.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> equipmentService.updateEquipment(equipment));
        assertEquals("The equipment does not exist", exception.getMessage());
    }

    // Test case for updating equipment with a duplicate entry
    @Test
    void testUpdateDuplicateEquipment() {
        when(equipmentRepository.findById(equipment.getId())).thenReturn(Optional.of(equipment));
        when(equipmentRepository.findByNameAndSupplierAndPurchaseDateAndWarranty(
                equipment.getName().trim(),
                equipment.getSupplier(),
                equipment.getPurchaseDate(),
                equipment.getWarranty()))
                .thenReturn(new Equipment()); // Simulate a duplicate equipment

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> equipmentService.updateEquipment(equipment));
        assertEquals("The equipment with the same name, supplier, purchase date, and warranty already exists",
                exception.getMessage());
    }

    // Test case for deleting equipment
    @Test
    void testDeleteEquipment() {
        when(equipmentRepository.findById(equipment.getId())).thenReturn(Optional.of(equipment));

        assertDoesNotThrow(() -> equipmentService.deleteEquipment(equipment.getId()));
        verify(equipmentRepository, times(1)).deleteById(equipment.getId());
    }

    // Test case for deleting a non-existing equipment
    @Test
    void testDeleteNonExistingEquipment() {
        when(equipmentRepository.findById(equipment.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> equipmentService.deleteEquipment(equipment.getId()));
        assertEquals("The equipment does not exist", exception.getMessage());
    }

    // Test case for validating equipment fields
    @Test
    void testCheckEquipmentFields_NullSupplier() {
        equipment.setSupplier(null);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> equipmentService.createEquipment(equipment));
        assertEquals("The supplier does not exist", exception.getMessage());
    }

    @Test
    void testCheckEquipmentFields_NegativePrice() {
        equipment.setPrice(-1.0);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> equipmentService.createEquipment(equipment));
        assertEquals("The price cannot be negative", exception.getMessage());
    }

    @Test
    void testCheckEquipmentFields_NegativeQuantity() {
        equipment.setQuantity(-1);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> equipmentService.createEquipment(equipment));
        assertEquals("The quantity cannot be negative", exception.getMessage());
    }

    @Test
    void testCheckEquipmentFields_FuturePurchaseDate() {
        equipment.setPurchaseDate(LocalDate.now().plusDays(1));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> equipmentService.createEquipment(equipment));
        assertEquals("The purchase date cannot be in the future", exception.getMessage());
    }
}
