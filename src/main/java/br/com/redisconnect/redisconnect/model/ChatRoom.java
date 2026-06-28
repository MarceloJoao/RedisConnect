package br.com.redisconnect.redisconnect.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    // Gerado automaticamente pelo backend
    private String id;

    @NotBlank(message = "Nome da sala é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String name;

    private String description;

    // Data e hora de criação no formato ISO-8601
    private String createdAt;
}
