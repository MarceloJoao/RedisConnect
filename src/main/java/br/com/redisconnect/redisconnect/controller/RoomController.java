package br.com.redisconnect.redisconnect.controller;

import br.com.redisconnect.redisconnect.model.ChatRoom;
import br.com.redisconnect.redisconnect.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // cria uma nova sala
    @PostMapping
    public ResponseEntity<ChatRoom> create(@Valid @RequestBody ChatRoom room) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.create(room));
    }

    // lista todas as salas
    @GetMapping
    public ResponseEntity<List<ChatRoom>> findAll() {
        return ResponseEntity.ok(roomService.findAll());
    }

    // busca uma sala pelo ID, retorna 404 se não encontrar
    @GetMapping("/{id}")
    public ResponseEntity<ChatRoom> findById(@PathVariable String id) {
        ChatRoom room = roomService.findById(id);
        if (room == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(room);
    }

    // remove uma sala pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
