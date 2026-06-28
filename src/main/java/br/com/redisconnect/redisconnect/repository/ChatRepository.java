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

    // Número máximo de mensagens mantidas por sala.
    private static final long MAX_MESSAGES = 100;

    // Prefixo das chaves de histórico no Redis.
    private static final String PREFIX_MESSAGES = "room:messages:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public ChatRepository(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void saveMessage(ChatMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            String key = PREFIX_MESSAGES + message.getRoomId();

            // Insere no início da lista (mensagem mais recente primeiro)
            redisTemplate.opsForList().leftPush(key, json);

            // Garante que a lista não cresce além de MAX_MESSAGES
            redisTemplate.opsForList().trim(key, 0, MAX_MESSAGES - 1);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar mensagem para o Redis", e);
        }
    }

    public List<ChatMessage> getHistory(String roomId, int limit) {
        String key = PREFIX_MESSAGES + roomId;
        List<String> jsons = redisTemplate.opsForList().range(key, 0, limit - 1);

        if (jsons == null || jsons.isEmpty()) {
            return Collections.emptyList();
        }

        List<ChatMessage> messages = new ArrayList<>();
        for (String json : jsons) {
            try {
                messages.add(objectMapper.readValue(json, ChatMessage.class));
            } catch (JsonProcessingException e) {
                // Ignora mensagens malformadas
            }
        }

        // Reverte para ordem cronológica (mais antiga → mais recente)
        Collections.reverse(messages);
        return messages;
    }
}
