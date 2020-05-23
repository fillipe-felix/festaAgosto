package com.pdv.repositories;

import com.pdv.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM tb_product p WHERE p.name = :desc " ,nativeQuery = true)
    Optional<Product> findByDesc(@Param("desc")String desc);

    



}
