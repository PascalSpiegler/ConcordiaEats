package com.jtspringproject.JtSpringProject.service;
import com.jtspringproject.JtSpringProject.model.Product;

import java.util.List;
import java.util.Optional;


public interface ProductService {
    Optional<Product> findById(int id);

    List<Object[]> findAllProducts();

    void loadProductsIntoRepository(List<Object[]> products);
}
