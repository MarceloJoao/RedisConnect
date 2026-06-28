package br.com.redisconnect.redisconnect.repository;

import br.com.redisconnect.redisconnect.model.ChatRoom;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class RoomRepository {

    private static final String PREFIX_ROOM = "room:";
    private static final String ROOM_IDS_SET = "rooms";
    private static final String ROOM_ID_SEQ = "room:id";

    private final StringRedisTemplate redisTemplate;

    public RoomRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // salva a sala no Redis e registra o ID no Set global
    public void save(ChatRoom room) {
        if (room.getId() == null) {
            Long id = redisTemplate.opsForValue().increment(ROOM_ID_SEQ);
            room.setId(id.toString());
        }

        String key = PREFIX_ROOM + room.getId();
        redisTemplate.opsForHash().put(key, "id",          room.getId());
        redisTemplate.opsForHash().put(key, "name",        room.getName());
        redisTemplate.opsForHash().put(key, "description", room.getDescription() != null ? room.getDescription() : "");
        redisTemplate.opsForHash().put(key, "createdAt",   room.getCreatedAt());
        redisTemplate.opsForSet().add(ROOM_IDS_SET, room.getId());
    }

    // busca os dados de uma sala pelo ID
    public ChatRoom findById(String id) {
        String key = PREFIX_ROOM + id;
        Map<Object, Object> data = redisTemplate.opsForHash().entries(key);

        if (data.isEmpty()) return null;

        ChatRoom room = new ChatRoom();
        room.setId(String.valueOf(data.get("id")));
        room.setName(String.valueOf(data.get("name")));
        room.setDescription(String.valueOf(data.get("description")));
        room.setCreatedAt(String.valueOf(data.get("createdAt")));
        return room;
    }

    // retorna todas as salas existentes
    public List<ChatRoom> findAll() {
        Set<String> ids = redisTemplate.opsForSet().members(ROOM_IDS_SET);
        List<ChatRoom> rooms = new ArrayList<>();

        if (ids == null || ids.isEmpty()) return rooms;

        for (String id : ids) {
            ChatRoom room = findById(id);
            if (room != null) rooms.add(room);
        }
        return rooms;
    }

    // remove a sala do Redis e do Set global
    public void delete(String id) {
        redisTemplate.delete(PREFIX_ROOM + id);
        redisTemplate.opsForSet().remove(ROOM_IDS_SET, id);
    }
}
