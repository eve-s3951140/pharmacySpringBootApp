package com.pharmacy.controllers.unit;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.pharmacy.services.ProductService;
import com.pharmacy.controllers.HomepageController;

import static org.mockito.Mockito.when;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(HomepageController.class)
class HomepageControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    // Test the controller to display the homepage
    @Test
    void testDisplayPage() throws Exception {
        // Mock the productService behavior
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/homepage"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("homepage"));
    }
}
