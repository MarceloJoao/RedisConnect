package br.com.redisconnect.redisconnect.model;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    private String id;
    private String roomId;
    private String senderId;
    private String content;
    private LocalDate timestamp;
    private LocalDateTime createdAt;
}
