package br.com.redisconnect.redisconnect.service;

import br.com.redisconnect.redisconnect.model.ChatMessage;
import br.com.redisconnect.redisconnect.repository.ChatRepository;
import br.com.redisconnect.redisconnect.websocket.RedisPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final RedisPublisher redisPublisher;
    private final ObjectMapper objectMapper;

    public ChatService(ChatRepository chatRepository,
                       RedisPublisher redisPublisher,
                       ObjectMapper objectMapper) {
        this.chatRepository = chatRepository;
        this.redisPublisher = redisPublisher;
        this.objectMapper = objectMapper;
    }

    // salva a mensagem no histórico e a publica no Pub/Sub
    public void send(ChatMessage message) {
        try {
            chatRepository.saveMessage(message);
            String json = objectMapper.writeValueAsString(message);
            redisPublisher.publish(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar mensagem", e);
        }
    }

    // retorna as últimas 50 mensagens da sala
    public List<ChatMessage> getHistory(String roomId) {
        return chatRepository.getHistory(roomId, 50);
    }
}
