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

    // recebe mensagens do cliente via WebSocket e as envia para o ChatService
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage chatMessage) {
        chatMessage.setId(UUID.randomUUID().toString()); // ID único gerado pelo backend
        chatMessage.setTimestamp(LocalDateTime.now().toString()); // timestamp gerado pelo backend
        chatService.send(chatMessage);
    }
}