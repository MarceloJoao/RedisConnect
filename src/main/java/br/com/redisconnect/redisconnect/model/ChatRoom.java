package br.com.redisconnect.redisconnect.model;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
