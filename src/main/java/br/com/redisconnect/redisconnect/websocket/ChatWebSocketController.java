package br.com.redisconnect.redisconnect.websocket;

import br.com.redisconnect.redisconnect.model.ChatMessage;
import br.com.redisconnect.redisconnect.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.UUID;



@Controller
public class ChatWebSocketController {

    private final ChatService chatService;

    public ChatWebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage chatMessage) {

        // O backend define o ID único e o timestamp exato da mensagem
        chatMessage.setId(UUID.randomUUID().toString());
        chatMessage.setTimestamp(LocalDateTime.now().toString());

        // Salva no histórico (Redis List) e entrega em tempo real (Pub/Sub)
        chatService.send(chatMessage);
    }
}