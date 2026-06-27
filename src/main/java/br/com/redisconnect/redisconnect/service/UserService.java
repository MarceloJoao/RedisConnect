package br.com.redisconnect.redisconnect.service;


import br.com.redisconnect.redisconnect.model.User;
import br.com.redisconnect.redisconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // Cadastra um novo usuário no Redis
    public void save(User user) {
        repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User findById(String id) {
       return repository.findById(id);
    }

    public List<User> findAll() {
       return repository.findAll();
    }

    public void update(User user) {
        repository.update(user);
    }

    public void delete(String id) {
        repository.delete(id);
    }

}
