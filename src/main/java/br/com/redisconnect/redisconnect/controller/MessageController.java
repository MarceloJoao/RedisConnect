package br.com.redisconnect.redisconnect.controller;

import br.com.redisconnect.redisconnect.model.ChatMessage;
import br.com.redisconnect.redisconnect.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final ChatService chatService;

    public MessageController(ChatService chatService) {
        this.chatService = chatService;
    }

    // retorna o histórico de mensagens de uma sala
    @GetMapping("/{roomId}")
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getHistory(roomId));
    }
}
