package com.pharmacy.services.servicesImplementation;

import java.util.Collection;

import com.pharmacy.models.Supplier;
import com.pharmacy.services.SupplierService;
import com.pharmacy.repositories.ProductRepository;
import com.pharmacy.repositories.SupplierRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SupplierServiceImpl implements SupplierService {

  private SupplierRepository supplierRepository;

  private ProductRepository productRepository;

  @Autowired
  public SupplierServiceImpl(SupplierRepository supplierRepository, ProductRepository productRepository) {
    this.supplierRepository = supplierRepository;
    this.productRepository = productRepository;
  }

  @Autowired
  public void setProductRepository(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  // Helper method to check if the supplier's phone number is valid
  private boolean isValidContact(String contact) {
    // Check if the contact is null or empty
    if (contact == null || contact.trim().isEmpty()) {
      return false;
    }

    // Check if the contact is a valid phone number (start with 04 followed by 8
    // digits)
    if (!contact.trim().matches("^04\\d{8}$")) {
      return false;
    }

    return true;
  }

  // Get all the suppliers
  @Override
  public Collection<Supplier> getAllSuppliers() {
    return supplierRepository.findAll();
  }

  // Create a new supplier
  @Override
  public void createSupplier(Supplier supplier) {
    // Check if the name or phone number of the supplier already used by another
    // supplier
    Supplier supplierWithSameName = supplierRepository.findByName(supplier.getName().trim());
    Supplier supplierWithSamePhoneNumber = supplierRepository.findByContact(supplier.getContact());

    // If the name is used by another supplier
    if (supplierWithSameName != null) {
      throw new RuntimeException("Name already used by another supplier");
    }

    // If the phone number is used by another supplier
    if (supplierWithSamePhoneNumber != null) {
      throw new RuntimeException("Phone number already used by another supplier");
    }

    // Check if the contact is valid
    if (!isValidContact(supplier.getContact())) {
      throw new RuntimeException("Invalid phone number (it must start with 04 followed by 8 digits)");
    }

    supplierRepository.save(supplier);
  }

  // Update a supplier
  @Override
  public void updateSupplier(Supplier supplier) {
    // Check if the supplier exists
    Supplier existingSupplier = supplierRepository.findById(supplier.getId()).orElse(null);

    // If the supplier does not exist
    if (existingSupplier == null) {
      throw new RuntimeException("Supplier does not exist");
    }

    // Check if the name or phone number of the supplier already used by another
    // supplier
    Supplier supplierWithSameName = supplierRepository.findByName(supplier.getName().trim());
    Supplier supplierWithSamePhoneNumber = supplierRepository.findByContact(supplier.getContact());

    // If the name is used by another supplier
    if (supplierWithSameName != null && supplierWithSameName.getId() != supplier.getId()) {
      throw new RuntimeException("Name already used by another supplier");
    }

    // If the phone number is used by another supplier
    if (supplierWithSamePhoneNumber != null && supplierWithSamePhoneNumber.getId() != supplier.getId()) {
      throw new RuntimeException("Phone number already used by another supplier");
    }

    // Check if the phone number is valid
    if (!isValidContact(supplier.getContact())) {
      throw new RuntimeException("Invalid phone number (it must start with 04 followed by 8 digits)");
    }

    supplierRepository.save(supplier);
  }

  // Delete a supplier
  @Override
  public void deleteSupplier(int id) {
    // Check if the supplier exists
    Supplier existingSupplier = supplierRepository.findById(id).orElse(null);

    // If the supplier does not exist
    if (existingSupplier == null) {
      throw new RuntimeException("supplier does not exist");
    }

    // Check if the supplier has associated products
    if (!productRepository.findBySupplierId(id).isEmpty()) {
      throw new RuntimeException("supplier has associated products");
    }

    supplierRepository.deleteById(id);
  }
}