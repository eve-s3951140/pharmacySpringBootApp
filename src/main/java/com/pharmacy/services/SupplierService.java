package com.pharmacy.services;

import java.util.Collection;

import com.pharmacy.models.Supplier;

public interface SupplierService {
  // Get all the suppliers
  public Collection<Supplier> getAllSuppliers();

  // Create a new supplier
  public void createSupplier(Supplier supplier);

  // Update a supplier
  public void updateSupplier(Supplier supplier);

  // Delete a supplier
  public void deleteSupplier(int id);
}