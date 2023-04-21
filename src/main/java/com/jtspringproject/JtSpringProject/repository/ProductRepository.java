package com.jtspringproject.JtSpringProject.repository;

import com.jtspringproject.JtSpringProject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ProductRepository extends JpaRepository <Product, Integer> {

    Optional<Product> findById(int id);



    @Query(value= "select * from Products ORDER BY product_id ASC", nativeQuery = true)
    List<Object[]> findAllProducts();

}
