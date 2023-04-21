package com.jtspringproject.JtSpringProject.service;

import com.jtspringproject.JtSpringProject.model.Product;
import com.jtspringproject.JtSpringProject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;

    public void loadProductsIntoRepository(List<Object[]> productsObjects) {
        List<Product> products = new ArrayList<>();
        for (Object[] productArr : productsObjects) {
            Product product = new Product();
            product.setId((int)productArr[0]);
            product.setRestaurant_id((int) productArr[1]);
            product.setName((String) productArr[2]);
            product.setImage((String) productArr[3]);
            product.setPrice((BigDecimal) productArr[4]);
            product.setDescription((String) productArr[5]);
            products.add(product);
        }

        productRepository.saveAll(products);
    }

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public List<Object[]> findAllProducts() {
        return productRepository.findAllProducts();
    }


    @Override
    public Optional<Product> findById(int id){
        return productRepository.findById(id);
    }



}
