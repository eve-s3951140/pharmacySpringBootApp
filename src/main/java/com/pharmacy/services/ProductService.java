package com.pharmacy.services;

import java.util.Collection;

import com.pharmacy.models.Product;

public interface ProductService {
  // Get all the products
  public Collection<Product> getAllProducts();

  // Get a list of products by the supplier's id
  public Collection<Product> getProductsBySupplierId(int supplierId);
}