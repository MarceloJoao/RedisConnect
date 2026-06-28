package br.com.redisconnect.redisconnect.repository;

import br.com.redisconnect.redisconnect.model.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ChatRepository {

    private static final long MAX_MESSAGES = 100;
    private static final String PREFIX_MESSAGES = "room:messages:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public ChatRepository(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    // salva a mensagem no início da lista e limita a 100 itens
    public void saveMessage(ChatMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            String key = PREFIX_MESSAGES + message.getRoomId();
            redisTemplate.opsForList().leftPush(key, json);
            redisTemplate.opsForList().trim(key, 0, MAX_MESSAGES - 1);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar mensagem", e);
        }
    }

    // retorna as últimas N mensagens em ordem cronológica
    public List<ChatMessage> getHistory(String roomId, int limit) {
        String key = PREFIX_MESSAGES + roomId;
        List<String> jsons = redisTemplate.opsForList().range(key, 0, limit - 1);

        if (jsons == null || jsons.isEmpty()) return Collections.emptyList();

        List<ChatMessage> messages = new ArrayList<>();
        for (String json : jsons) {
            try {
                messages.add(objectMapper.readValue(json, ChatMessage.class));
            } catch (JsonProcessingException e) {
                // ignora mensagens corrompidas
            }
        }

        Collections.reverse(messages); // mais antiga primeiro
        return messages;
    }
}
