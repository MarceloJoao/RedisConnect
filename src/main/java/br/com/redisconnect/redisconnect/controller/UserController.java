package br.com.redisconnect.redisconnect.controller;

import br.com.redisconnect.redisconnect.model.User;
import br.com.redisconnect.redisconnect.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    // Camada responsável pelas regras de negócio dos usuários.
    private final UserService userService;

    // O Spring injeta automaticamente o UserService.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint responsável por cadastrar um novo usuário.
    @PostMapping
    public User register(@RequestBody User user) {

        // Envia o usuário para a camada de serviço.
        userService.register(user);

        // Retorna o usuário cadastrado.
        return user;
    }
}