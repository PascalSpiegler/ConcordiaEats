package com.jtspringproject.JtSpringProject.service;
import com.jtspringproject.JtSpringProject.model.Product;
import com.jtspringproject.JtSpringProject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional

public class BasketServiceImpl implements BasketService {
    private final ProductRepository productRepository;

    private Map<Product, Integer> products = new HashMap<>();

    @Autowired
    public BasketServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(Product product) {
        System.out.println(products.get(product));
        if (products.containsKey(product)) {
            products.replace(product, products.get(product) + 1);
        } else {
            products.put(product, 1);
        }
        System.out.println(products);

    }

    @Override
    public void removeProduct(Product product){
        if (products.containsKey(product)){
            if (products.get(product) > 1){
                products.replace(product, products.get(product) - 1);
            }
            else if (products.get(product) == 1){
                products.remove(product);
            }
        }
        System.out.println(products);
    }

    @Override
    public Map<Product, Integer> getProductsInBasket() {
        return Collections.unmodifiableMap(products);
    }

    @Override
    public int getQuantityInBasketById(int id) {
        Map<Product, Integer> basket = getProductsInBasket();
        for (Map.Entry<Product, Integer> entry : basket.entrySet()) {
            if (entry.getKey().getId() == id) {
                return entry.getValue();
            }
        }
        return 0;
    }

    @Override
    public void clearBasket(){
        this.products = new HashMap<>();
    }

}
