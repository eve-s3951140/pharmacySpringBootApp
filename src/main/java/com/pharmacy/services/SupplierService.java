package com.pharmacy.services;

import java.util.Collection;

import com.pharmacy.models.Supplier;

public interface SupplierService {
  // Get all the suppliers
  public Collection<Supplier> getAllSuppliers();

  // Create a new supplier or update an existing supplier
  public void createOrUpdateSupplier(Supplier supplier);

  // Delete a supplier
  public void deleteSupplier(int id);
}