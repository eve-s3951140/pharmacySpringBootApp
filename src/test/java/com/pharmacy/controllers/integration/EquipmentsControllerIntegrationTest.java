package com.pharmacy.controllers.integration;

import java.time.LocalDate;

import com.pharmacy.models.Supplier;
import com.pharmacy.models.Equipment;
import com.pharmacy.services.SupplierService;
import com.pharmacy.services.EquipmentService;
import com.pharmacy.repositories.EquipmentRepository;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class EquipmentsControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipmentService equipmentService;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private EquipmentRepository equipmentRepository;

    @Test
    void testHomepage() throws Exception {
        mockMvc.perform(get("/equipments"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("equipments"))
                .andExpect(model().attributeExists("suppliers"))
                .andExpect(view().name("equipments"));
    }

    @Test
    void testAddEquipment_Success() throws Exception {
        // Create a mock supplier or a real supplier object as needed
        Supplier supplier = new Supplier("Test Supplier", "123456");

        // Create the equipment object with all required parameters
        Equipment equipment = new Equipment("1 year", LocalDate.now(), "Test Equipment", 10, 199.99, supplier);

        mockMvc.perform(post("/equipments/add")
                .flashAttr("equipment", equipment))
                .andExpect(redirectedUrl("/equipments"))
                .andExpect(flash().attribute("message", "Equipment added successfully"));

        // Verify that the equipment was saved in the database
        assert !equipmentRepository.findAll().isEmpty();
    }
}
