package br.com.redisconnect.redisconnect.model;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
public class ChatRoom {

    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
