package br.com.redisconnect.redisconnect.repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    // Set que mantém todos os IDs de usuários cadastrados.
    // Usado no findAll() para evitar o uso de KEYS *, que bloqueia o Redis.
    private static final String USER_IDS_SET = "users:ids";

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

        // Registra o ID no Set global de usuários.
        // Redis: SADD users:ids {id}
        redisTemplate.opsForSet().add(USER_IDS_SET, user.getId());
    }

    public User findById(String id) {
        //chave do usuario
        String key = PREFIX_USER + id;

        //recuperar os dados do hash
        Map<Object, Object> user = redisTemplate.opsForHash().entries(key);

        if (user.isEmpty()) {
            return null;
        }

        User userEntity = new User();
        userEntity.setId(String.valueOf(user.get("id")));
        userEntity.setName(String.valueOf(user.get("name")));
        userEntity.setEmail(String.valueOf(user.get("email")));
        userEntity.setPassword(String.valueOf(user.get("password")));

        return userEntity;

    }

    public User findByEmail(String email) {
        //chave do email
        String key = PREFIX_EMAIL + email;

        //recupera o id associado ao email
        String id = redisTemplate.opsForValue().get(key);

        if(id == null) return null;

        return findById(id);
    }

    public List<User> findAll() {
        // Busca os IDs a partir do Set dedicado.
        // Redis: SMEMBERS users:ids
        // Evita o uso de KEYS *, que varre todo o keyspace e bloqueia o Redis.
        Set<String> ids = redisTemplate.opsForSet().members(USER_IDS_SET);
        List<User> users = new ArrayList<>();

        if (ids == null || ids.isEmpty()) return users;

        for (String id : ids) {
            User user = findById(id);
            if (user != null) users.add(user);
        }
        return users;
    }

    public void update(User user) {

        String key = PREFIX_USER + user.getId();

        // Remove o índice do e-mail antigo antes de atualizar,
        // evitando que índices órfãos fiquem acumulados no Redis.
        User existing = findById(user.getId());
        if (existing != null && !existing.getEmail().equals(user.getEmail())) {
            redisTemplate.delete(PREFIX_EMAIL + existing.getEmail());
        }

        redisTemplate.opsForHash().put(key, "name", user.getName());
        redisTemplate.opsForHash().put(key, "email", user.getEmail());
        redisTemplate.opsForHash().put(key, "password", user.getPassword());

        // Cria (ou mantém) o índice com o e-mail atualizado.
        redisTemplate.opsForValue()
                .set(PREFIX_EMAIL + user.getEmail(), user.getId());
    }

    public void delete(String id) {
        User user = findById(id);
        if (user == null) {
            return;
        }
        // Remove o Hash do usuário.
        redisTemplate.delete(PREFIX_USER + id);

        // Remove o índice por e-mail.
        redisTemplate.delete(PREFIX_EMAIL + user.getEmail());

        // Remove o ID do Set global.
        // Redis: SREM users:ids {id}
        redisTemplate.opsForSet().remove(USER_IDS_SET, id);
    }
}