package com.pharmacy.services.servicesImplementation;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.pharmacy.models.Supplier;
import com.pharmacy.repositories.SupplierRepository;
import com.pharmacy.services.SupplierService;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SupplierServiceImpl implements SupplierService {

  private SupplierRepository supplierRepository;

  @Autowired
  public SupplierServiceImpl(SupplierRepository supplierRepository) {
    this.supplierRepository = supplierRepository;
  }

  @Override
  public Collection<Supplier> getAllSuppliers() {
    return supplierRepository.findAll();
  }
}