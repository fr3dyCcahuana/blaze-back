package com.mdp.api.test.testblaze.controllers;

import com.mdp.api.test.testblaze.entities.Product;
import com.mdp.api.test.testblaze.services.IProductService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping()
    public ResponseEntity<?> showProducts() {

        List<Product> products = null;
        Map<String, Object> map = new HashMap<>();

        try {
            products = productService.listAll();
        } catch (DataException e) {
            map.put("message", "Error en la consulta");
            return new ResponseEntity<List<Product>>(products, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<?> showProducts(@PathVariable(value = "page") Long page) {

        Page<Product> products = null;
        Pageable pageable = null;
        Map<String, Object> map = new HashMap<>();

        try {
            pageable = PageRequest.of(page.intValue() - 1, 2);
            products = productService.listAll(pageable);
        } catch (DataException e) {
            map.put("message", "Error en la consulta");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Page<Product>>(products, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> showProductById(@PathVariable(value = "id") Long id) {

        Product product = null;
        Map<String, Object> map = new HashMap<>();

        try {
            product = productService.findById(id);
        } catch (DataException e) {
            map.put("message", "Error en la consulta");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }

        if (product == null) {
            map.put("message", "Producto no encontrado en la BD");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> saveProduct(@RequestBody Product productRequest) {

        Product product = null;
        Map<String, Object> map = new HashMap<>();

        try {
            product = productService.saveProduct(productRequest);

        } catch (DataException d) {
            map.put("message", "Error al insertar un Producto");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }
        if (product == null) {
            map.put("message", "Producto no encontrad en la BD");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long id) {

        Map<String, Object> map = new HashMap<>();

        try {
            productService.deleteById(id);
        } catch (DataException d) {
            map.put("message", "Error al eliminar un Producto");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateProduct(@PathVariable(value = "id") Long id, @RequestBody Product productRequest) {

        Product product = null;
        Product productResponse = null;
        Map<String, Object> map = new HashMap<>();

        try {
            product = productService.findById(id);

            product.setName((productRequest.getName() != null) ? productRequest.getName() : product.getName());
            product.setCategorie((productRequest.getCategorie() != null) ? productRequest.getCategorie() : product.getCategorie());
            product.setUnitPrice((productRequest.getUnitPrice() != null) ? productRequest.getUnitPrice() : product.getUnitPrice());
            product.setStatus((productRequest.isStatus() != product.isStatus()) ? productRequest.isStatus() : product.isStatus());
            productResponse = productService.saveProduct(product);

        } catch (DataException d) {
            map.put("message", "Error al modificar un Producto");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }
        if (product == null) {
            map.put("message", "Producto no encontrado en la BD");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Product>(productResponse, HttpStatus.OK);
    }
}
