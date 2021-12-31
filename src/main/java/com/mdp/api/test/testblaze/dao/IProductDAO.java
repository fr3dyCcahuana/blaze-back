package com.mdp.api.test.testblaze.dao;

import com.mdp.api.test.testblaze.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductDAO extends JpaRepository<Product,Long> {
}
