package com.mdp.api.test.testblaze.services;

import com.mdp.api.test.testblaze.dao.IProductDAO;
import com.mdp.api.test.testblaze.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductDAO productDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Product> listAll() {
        return (List<Product>) productDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> listAll(Pageable pageable) {
        return productDAO.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional()
    public Product saveProduct(Product product) {
        return productDAO.save(product);
    }

    @Override
    @Transactional()
    public void deleteById(Long id) {
        productDAO.deleteById(id);
    }
}
