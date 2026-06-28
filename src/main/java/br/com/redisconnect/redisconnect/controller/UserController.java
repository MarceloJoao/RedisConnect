package br.com.redisconnect.redisconnect.controller;

import br.com.redisconnect.redisconnect.model.User;
import br.com.redisconnect.redisconnect.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User save(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping("/email/{email}")
    public User findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }

    // Endpoint responsável por validar o Login
    @PostMapping("/login")
    public org.springframework.http.ResponseEntity<?> login(@RequestBody User loginData) {
        User user = userService.findByEmail(loginData.getEmail());

        if (user != null && user.getPassword().equals(loginData.getPassword())) {
            return org.springframework.http.ResponseEntity.ok(user);
        }

        return org.springframework.http.ResponseEntity.status(401).body("E-mail ou senha incorretos.");
    }
}