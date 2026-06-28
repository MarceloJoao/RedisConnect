package br.com.redisconnect.redisconnect.model;
import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    // Gerado automaticamente pelo backend — não validado na entrada
    private String id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 80, message = "Nome deve ter entre 2 e 80 caracteres")
    private String name;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;
}
