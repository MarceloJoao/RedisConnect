package br.com.redisconnect.redisconnect.service;

import br.com.redisconnect.redisconnect.model.ChatRoom;
import br.com.redisconnect.redisconnect.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    // cria uma nova sala definindo a data de criação no backend
    public ChatRoom create(ChatRoom room) {
        room.setCreatedAt(LocalDateTime.now().toString());
        repository.save(room);
        return room;
    }

    // busca uma sala pelo ID
    public ChatRoom findById(String id) {
        return repository.findById(id);
    }

    // retorna todas as salas
    public List<ChatRoom> findAll() {
        return repository.findAll();
    }

    // remove uma sala pelo ID
    public void delete(String id) {
        repository.delete(id);
    }
}
