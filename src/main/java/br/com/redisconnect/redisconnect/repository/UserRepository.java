package br.com.redisconnect.redisconnect.repository;

import br.com.redisconnect.redisconnect.model.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    // Prefixo utilizado para armazenar os usuários
    private static final String PREFIX_USER = "user:";

    // Prefixo utilizado para criar um índice por e-mail.
    private static final String PREFIX_EMAIL = "user:email:";

    // Chave responsável por gerar IDs sequenciais.
    private static final String USER_ID_SEQUENCE = "user:id";

    // Responsável pela comunicação entre a aplicação e o Redis.
    private final StringRedisTemplate redisTemplate;

    public UserRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Salva um usuário no Redis.
    public void save(User user) {

        // Caso o usuário não possua um ID, gera um ID sequencial utilizando o Redis.
        if (user.getId() == null) {
            Long id = redisTemplate.opsForValue().increment(USER_ID_SEQUENCE);
            user.setId(id.toString());
        }

        // Monta a chave principal do usuário.
        String key = PREFIX_USER + user.getId();

        // Armazena os dados do usuário utilizando um Hash.
        redisTemplate.opsForHash().put(key, "id", user.getId());
        redisTemplate.opsForHash().put(key, "name", user.getName());
        redisTemplate.opsForHash().put(key, "email", user.getEmail());
        redisTemplate.opsForHash().put(key, "password", user.getPassword());

        // Cria um índice para localizar rapidamente um usuário pelo e-mail.
        redisTemplate.opsForValue().set(
                PREFIX_EMAIL + user.getEmail(),
                user.getId()
        );
    }
}