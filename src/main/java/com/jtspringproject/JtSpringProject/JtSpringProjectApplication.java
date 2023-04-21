package com.jtspringproject.JtSpringProject;
import com.jtspringproject.JtSpringProject.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class JtSpringProjectApplication {
	private static ProductService productService = null;

	public JtSpringProjectApplication(ProductService productService) {
		this.productService = productService;
	}

	public static void main(String[] args) {
		SpringApplication.run(JtSpringProjectApplication.class, args);
		List<Object[]> loadProducts = productService.findAllProducts();
		productService.loadProductsIntoRepository(loadProducts);
	}

}
