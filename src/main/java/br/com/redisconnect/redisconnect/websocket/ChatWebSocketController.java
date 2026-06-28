package br.com.redisconnect.redisconnect.websocket;

import br.com.redisconnect.redisconnect.model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class ChatWebSocketController {

    private final RedisPublisher redisPublisher;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public ChatWebSocketController(RedisPublisher redisPublisher) {
        this.redisPublisher = redisPublisher;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage chatMessage) {
        System.out.println(">>> MENSAGEM RECEBIDA NO BACKEND: " + chatMessage.getContent());

        try {
            // O Backend define a data exata
            chatMessage.setTimestamp(LocalDate.from(LocalDateTime.now()));

            String jsonMessage = objectMapper.writeValueAsString(chatMessage);
            redisPublisher.publish(jsonMessage);
            System.out.println(">>> MENSAGEM PUBLICADA NO REDIS COM SUCESSO");

        } catch (Exception e) {
            System.out.println("Erro ao converter mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
}