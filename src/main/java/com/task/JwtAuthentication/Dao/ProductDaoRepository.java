package com.task.JwtAuthentication.Dao;

import com.task.JwtAuthentication.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDaoRepository extends JpaRepository<Product,Integer>
{
    // All methods generated Automatically
    List<Product> findByUserUuid(String uuid);
}
