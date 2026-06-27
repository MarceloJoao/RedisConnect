package br.com.redisconnect.redisconnect.service;


import br.com.redisconnect.redisconnect.model.User;
import br.com.redisconnect.redisconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // Cadastra um novo usuário no Redis
    public void register(User user) {
        repository.save(user);
    }
}
