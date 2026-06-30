# RedisConnect 💬

**RedisConnect** é um chat em tempo real desenvolvido como projeto acadêmico para a disciplina **IMD1130 - BANCOS DE DADOS NOSQL**. O objetivo é demonstrar o uso do **Redis como banco de dados principal** (e não apenas como cache), utilizando suas estruturas de dados nativas para armazenar usuários, salas e mensagens, além do **Pub/Sub** para distribuição de mensagens em tempo real.

## Funcionalidades

- Cadastro e autenticação de usuários
- Criação e listagem de salas de chat
- Envio e recebimento de mensagens em tempo real (WebSocket + STOMP)
- Histórico de mensagens por sala (limitado às últimas 100)

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.0 |
| Redis | 8 |
| Thymeleaf | — |
| WebSocket (STOMP + SockJS) | — |
| Maven | 3.9.16 |
| Docker | — |
| Render (deploy) | — |

## Como executar localmente

### Pré-requisitos

- Java 21+
- Docker e Docker Compose

### Passos

```bash
# 1. Inicie o Redis
docker compose up -d

# 2. Execute a aplicação
./mvnw spring-boot:run
```

Acesse [http://localhost:8080](http://localhost:8080).

### Com build manual

```bash
./mvnw clean package -DskipTests
java -jar target/redisconnect-0.0.1-SNAPSHOT.jar
```

## Deploy

Acesse a aplicação em produção: [https://redisconnect.onrender.com/](https://redisconnect.onrender.com/)

> **Nota:** O plano gratuito do Render hiberna a aplicação após 15 minutos de inatividade. O primeiro acesso pode levar alguns segundos (cold start).

## Integrantes

| Nome | Matrícula |
|---|---|
| GERALDO LUCAS BEZERRA ROCHA | 20200047480 |
| JOAO MARCELO DE SOUZA | 20220030819 |

---

**Disciplina:** IMD1130 - BANCOS DE DADOS NOSQL - T01 (2026.1)  
**Docente:** GUSTAVO BEZERRA PAZ LEITAO
