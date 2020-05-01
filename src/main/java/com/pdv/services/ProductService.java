package com.pdv.services;

import com.pdv.entities.Product;
import com.pdv.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        return obj.get();
    }

    public Product insert(Product obj) {
        return productRepository.save(obj);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public Product update(Long id, Product obj) {
        //O getOne apenas prepara o objeto, nao pega ele direto no banco de dados
        Product entity = productRepository.getOne(id);
        updateData(entity, obj);
        return productRepository.save(entity);
    }

    private void updateData(Product entity, Product obj) {
        entity.setDescription(obj.getDescription());
        entity.setName(obj.getName());
        entity.setPrice(obj.getPrice());
    }

}
