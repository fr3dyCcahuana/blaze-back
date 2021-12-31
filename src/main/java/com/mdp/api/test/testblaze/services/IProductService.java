package com.mdp.api.test.testblaze.services;

import com.mdp.api.test.testblaze.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional()
public interface IProductService {

    List<Product> listAll();

    Page<Product> listAll(Pageable pageable);

    Product findById(Long id);

    Product saveProduct(Product product);

    void deleteById(Long id);
}
