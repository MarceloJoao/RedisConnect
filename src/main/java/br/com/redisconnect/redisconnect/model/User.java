package br.com.redisconnect.redisconnect.model;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    //indentificador
    private String id;
    //nome
    private String name;
    //email
    private String email;
    //senha
    private String password;
    //data de criação
    private LocalDateTime createdAt;
}
