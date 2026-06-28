package br.com.redisconnect.redisconnect.model;

import lombok.*;

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
    private String timestamp;
}
