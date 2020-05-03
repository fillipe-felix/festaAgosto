package com.pdv.services;

import com.pdv.entities.User;
import com.pdv.repositories.UserRepository;
import com.pdv.services.exceptions.DatabaseException;
import com.pdv.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public User insert(User obj) {
        return userRepository.save(obj);
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            //pega a exceção de id nao encontrado
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            //pega a exceção de vioalação de constraint do banco de dados
            throw new DatabaseException(e.getMessage());
        }

    }

    public User update(Long id, User obj) {
        //O getOne apenas prepara o objeto, nao pega ele direto no banco de dados
        try {
            User entity = userRepository.getOne(id);
            updateData(entity, obj);
            return userRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(User entity, User obj) {
        entity.setName(obj.getName());
        entity.setEmail(obj.getEmail());
        entity.setPhone(obj.getPhone());
    }
}
