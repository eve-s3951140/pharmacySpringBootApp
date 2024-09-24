package com.pharmacy.services;

import java.util.Collection;

import com.pharmacy.models.Product;

public interface ProductService {
  // Get all the products
  public Collection<Product> getAllProducts();
}