package com.pharmacy.services.unit;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import com.pharmacy.models.Product;
import com.pharmacy.models.Supplier;
import com.pharmacy.repositories.ProductRepository;
import com.pharmacy.services.servicesImplementation.ProductServiceImpl;

@SpringBootTest
class ProductServiceUnitTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    // Mock subclass of Product class
    private class ProductMock extends Product {
        // Default constructor
        public ProductMock() {
            super();
        }

        // Constructor with parameters
        public ProductMock(String name, int quantity, double price, Supplier supplier) {
            super(name, quantity, price, supplier);
        }

        // Get the product type for the mock product
        @Override
        public String getProductType() {
            return "TestProduct";
        }
    }

    // Test the service to construct a product object and verify the data
    @Test
    void testProductConstructor() {
        // Create a mock Supplier instance and a Product instance with parameters
        Supplier supplier = new Supplier();
        ProductMock product = new ProductMock("Test Product", 50, 10.0, supplier);

        // Verify the product properties
        assertEquals("Test Product", product.getName());
        assertEquals(50, product.getQuantity());
        assertEquals(10.0, product.getPrice());
        assertEquals(supplier, product.getSupplier());
    }

    // Test the service to
    @Test
    void testProductProperties() {
        // Create a mock Supplier instance and a Product instance
        Supplier supplier = new Supplier();
        ProductMock product = new ProductMock();

        // Set the product properties
        product.setName("Test Product");
        product.setQuantity(100);
        product.setPrice(25.5);
        product.setSupplier(supplier);

        // Verify the product properties
        assertEquals("Test Product", product.getName());
        assertEquals(100, product.getQuantity());
        assertEquals(25.5, product.getPrice());
        assertEquals(supplier, product.getSupplier());
    }

    // Test the service to get all products
    @Test
    void testGetAllProducts() {
        // Create a mock supplier instance and two product instances
        Supplier supplier = new Supplier();
        ProductMock product1 = new ProductMock("Product1", 10, 20.0, supplier);
        ProductMock product2 = new ProductMock("Product2", 20, 30.0, supplier);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // Call the getAllProducts method
        Collection<Product> products = productService.getAllProducts();

        // Verify the products list is not null and has a size of 2
        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    // Test the service to get products by supplier id
    @Test
    void testGetProductsBySupplierId() {
        // Set the supplier id to be used in the test
        int supplierId = 1;

        // Create a mock supplier instance and two product instances
        Supplier supplier = new Supplier();
        ProductMock product1 = new ProductMock("Product1", 10, 20.0, supplier);
        ProductMock product2 = new ProductMock("Product2", 20, 30.0, supplier);
        when(productRepository.findBySupplierId(supplierId)).thenReturn(Arrays.asList(product1, product2));

        // Call the getProductsBySupplierId method
        Collection<Product> products = productService.getProductsBySupplierId(supplierId);

        // Verify the products list is not null and has a size of 2
        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findBySupplierId(supplierId);
    }
}
