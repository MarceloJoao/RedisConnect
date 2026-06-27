package br.com.redisconnect.redisconnect.model;

import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
public class ChatMessage {

    private String id;
    private String roomId;
    private String senderId;
    private String content;
    private LocalDate timestamp;
    private LocalDateTime createdAt;
}
