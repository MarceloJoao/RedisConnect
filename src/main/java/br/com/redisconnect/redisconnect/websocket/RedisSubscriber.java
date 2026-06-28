package br.com.redisconnect.redisconnect.websocket;

import br.com.redisconnect.redisconnect.model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishedMessage = new String(message.getBody());
            ChatMessage chatMessage = objectMapper.readValue(publishedMessage, ChatMessage.class);

            // Repassa para a sala específica
            messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);

        } catch (Exception e) {
            System.out.println("Erro ao processar mensagem do Redis: " + e.getMessage());
            e.printStackTrace();
        }
    }
}