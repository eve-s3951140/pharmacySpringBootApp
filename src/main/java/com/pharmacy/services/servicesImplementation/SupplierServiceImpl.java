package com.pharmacy.services.servicesImplementation;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.pharmacy.models.Supplier;
import com.pharmacy.services.SupplierService;
import com.pharmacy.repositories.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SupplierServiceImpl implements SupplierService {

  private SupplierRepository supplierRepository;

  @Autowired
  public SupplierServiceImpl(SupplierRepository supplierRepository) {
    this.supplierRepository = supplierRepository;
  }

  // Get all the suppliers
  @Override
  public Collection<Supplier> getAllSuppliers() {
    return supplierRepository.findAll();
  }

  // Create a new supplier
  @Override
  public void createSupplier(Supplier supplier) {
    // Check if the name or phone number of the supplier already used by another supplier
    Supplier supplierWithSameName = supplierRepository.findByName(supplier.getName());
    Supplier supplierWithSamePhoneNumber = supplierRepository.findByContact(supplier.getContact());

    // If the name is used by another supplier
    if (supplierWithSameName != null) {
      throw new RuntimeException("Name already used by another supplier");
    }

    // If the phone number is used by another supplier
    if (supplierWithSamePhoneNumber != null) {
      throw new RuntimeException("Phone number already used by another supplier");
    }

    supplierRepository.save(supplier);
  }

  // Update a supplier
  @Override
  public void updateSupplier(Supplier supplier) {
    // Check if the name or phone number of the supplier already used by another supplier
    Supplier supplierWithSameName = supplierRepository.findByName(supplier.getName());
    Supplier supplierWithSamePhoneNumber = supplierRepository.findByContact(supplier.getContact());

    // If the name is used by another supplier
    if (supplierWithSameName != null && supplierWithSameName.getId() != supplier.getId()) {
      throw new RuntimeException("Name already used by another supplier");
    }

    // If the phone number is used by another supplier
    if (supplierWithSamePhoneNumber != null && supplierWithSamePhoneNumber.getId() != supplier.getId()) {
      throw new RuntimeException("Phone number already used by another supplier");
    }

    supplierRepository.save(supplier);
  }

  // Delete a supplier
  @Override
  public void deleteSupplier(int id) {
    supplierRepository.deleteById(id);
  }
}