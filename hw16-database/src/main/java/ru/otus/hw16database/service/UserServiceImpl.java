package ru.otus.hw16database.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw16database.entity.User;
import ru.otus.hw16database.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    
    private UserRepository userRepository;
    
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Transactional
    @Override
    public Optional<User> get(long id) {
        return userRepository.findById(id);
    }
    
    @Transactional
    @Override
    public void save(User object) {
        userRepository.save(object);
    }
    
    @Transactional
    @Override
    public void update(User object) {
        userRepository.save(object);
    }
    
    @Transactional
    @Override
    public void delete(User object) {
        userRepository.delete(object);
    }
    
    @Transactional
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
