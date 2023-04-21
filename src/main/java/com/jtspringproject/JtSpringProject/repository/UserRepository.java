package com.jtspringproject.JtSpringProject.repository;

import com.jtspringproject.JtSpringProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value= "select * from Users where user_id = :id", nativeQuery = true)
    User findById(@Param("id") int id);


}

