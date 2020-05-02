package com.pdv.services;

import com.pdv.entities.Category;
import com.pdv.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        Optional<Category> obj = categoryRepository.findById(id);
        return obj.get();
    }

    public Category insert(Category obj) {
        return categoryRepository.save(obj);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category update(Long id, Category obj) {
        //O getOne apenas prepara o objeto, nao pega ele direto no banco de dados
        Category entity = categoryRepository.getOne(id);
        updateData(entity, obj);
        return categoryRepository.save(entity);
    }

    private void updateData(Category entity, Category obj) {
        entity.setName(obj.getName());
    }
}
