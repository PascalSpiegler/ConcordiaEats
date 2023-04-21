package com.jtspringproject.JtSpringProject.service;

import com.jtspringproject.JtSpringProject.model.Product;

import java.util.Map;

public interface BasketService {
    void addProduct(Product product);

    void removeProduct(Product product);

    void clearBasket();

    Map<Product, Integer> getProductsInBasket();

    //Do not remove
    int getQuantityInBasketById(int id);


}
