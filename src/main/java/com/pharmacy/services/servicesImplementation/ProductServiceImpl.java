package com.pharmacy.services.servicesImplementation;

import java.util.Collection;

import com.pharmacy.models.Product;
import com.pharmacy.services.ProductService;
import com.pharmacy.repositories.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProductServiceImpl implements ProductService {

  private ProductRepository productRepository;

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public Collection<Product> getAllProducts() {
    return productRepository.findAll();
  }
}