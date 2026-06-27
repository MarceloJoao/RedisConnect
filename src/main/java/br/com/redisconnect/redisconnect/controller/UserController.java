package br.com.redisconnect.redisconnect.controller;

import br.com.redisconnect.redisconnect.model.User;
import br.com.redisconnect.redisconnect.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public User save(@RequestBody User user) {
        // Envia o usuário para a camada de serviço.
        userService.save(user);
        // Retorna o usuário cadastrado.
        return user;
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable String id) {
        return userService.findById(id);
    }

    // Busca um usuário pelo e-mail.
    @GetMapping("/email/{email}")
    public User findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    // Lista todos os usuários.
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    // Atualiza um usuário.
    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return user;
    }
    // Exclui um usuário.
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }
}