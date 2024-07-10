package com.dimas.core.service;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.rabbitmq.RabbitMQMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

@Slf4j
@ApplicationScoped
public class WebsocketService implements ISubscriptionHandler {

    private final Map<UUID, Session> sessionMap;

    public WebsocketService(Map<UUID, Session> sessionMap) {
        this.sessionMap = sessionMap;
    }

    public WebsocketService() {
        this.sessionMap = new ConcurrentHashMap<>();
    }

    public void addSession(UUID userId, Session session) {
        sessionMap.put(userId, session);
    }


    public Uni<Void> handle(UUID userId, RabbitMQMessage rabbitMQMessage) {
        return Uni.createFrom().item(rabbitMQMessage)
                .onItem().transformToUni(msg -> {
                    var body = msg.body().toString();
                    log.info("WEBSOCKET handler: Received for userId={} Received body={}", userId, body);
                    return doWebsocketPublish(userId, body)
                            .onItem().invoke(ex -> log.info("Successfully published to websocket for userId={}", userId))
                            .onFailure().invoke(ex -> log.error("Failed to published to websocket for userId={}", userId))
                            .replaceWithVoid();
                });
    }

    @SneakyThrows
    public Uni<Void> doWebsocketPublish(UUID userId, String post) {
        var session = sessionMap.get(userId);
        if (isNull(session)) {
            log.debug("UserId={} is not connected to websocket ignore.", userId);
            return Uni.createFrom().voidItem();
        };
        log.debug("Publishing data for userId={}", userId);
        session.getAsyncRemote().sendObject(post, result -> {
            if (result.getException() != null) {
                log.error("Unable to send message: ", result.getException());
            }
        });
        return Uni.createFrom().voidItem();
    }

}
