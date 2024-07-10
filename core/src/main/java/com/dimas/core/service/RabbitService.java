package com.dimas.core.service;

import com.dimas.core.domain.entity.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.rabbitmq.RabbitMQClient;
import io.vertx.mutiny.rabbitmq.RabbitMQMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class RabbitService {

    public final static String NEW_POST_EXCHANGE = "otus.new-posts";
    private final RabbitMQClient rabbitMQClient;
    private final ObjectMapper objectMapper;

    void onStart(@Observes StartupEvent event) {
        rabbitMQClient.start()
                .onItem().invoke(v -> {
                    var isConnected = rabbitMQClient.isConnected();
                    var isOpenChannel = rabbitMQClient.isOpenChannel();
                    log.info("On item: isConnected={}, isOpenChannel={}", isConnected, isOpenChannel);
                })
                .chain(v -> declareExchange())
                .onSubscription().invoke(v -> {
                    var isConnected = rabbitMQClient.isConnected();
                    var isOpenChannel = rabbitMQClient.isOpenChannel();
                    log.info("On subscription: isConnected={}, isOpenChannel={}", isConnected, isOpenChannel);
                })
                .onFailure().invoke(th -> log.info("Failed to start RabbitMQClient", th))
                .subscribe().with(v -> {
                });
    }

    void onStop(@Observes ShutdownEvent event) {
        log.info("stopping rabbitmq client");
        rabbitMQClient.stopAndForget();
    }

    public Uni<Void> declareExchange() {
        return rabbitMQClient.exchangeDeclare(NEW_POST_EXCHANGE, BuiltinExchangeType.TOPIC.getType(), true, false)
                .onItem().invoke(v -> log.info("Exchange {} created", NEW_POST_EXCHANGE))
                .onFailure().invoke(ex -> log.error("Failed to create exchange {}", NEW_POST_EXCHANGE, ex));
    }

    public Uni<Void> declareQueue(String queue) {
        return rabbitMQClient.queueDeclare(queue, false, true, true)
                .onItem().invoke(declareOk -> log.info("Queue declaredOk={}", declareOk))
                .onFailure().invoke(ex -> log.error("Failed to declare queue={}", queue, ex))
                .replaceWithVoid();
    }

    public Uni<Void> declareBinding(String queue, String routing) {
        return declareBinding(queue, NEW_POST_EXCHANGE, routing);
    }

    public Uni<Void> declareBinding(String queue, String exchange, String routing) {
        return rabbitMQClient.queueBind(queue, exchange, routing)
                .onItem().invoke(v -> log.info("Binding queue={} to exchange={} with routing={}}", queue, exchange, routing))
                .onFailure().invoke(ex -> log.error("Failure binding queue={} to exchange={} with routing={}}", queue, exchange, routing, ex));
    }

    public Uni<Void> unbind(String queue, String routing) {
        return unbind(queue, NEW_POST_EXCHANGE, routing);
    }

    public Uni<Void> unbind(String queue, String exchange, String routing) {
        return rabbitMQClient.queueUnbind(queue, exchange, routing)
                .onItem().invoke(v -> log.info("Unbinding queue={} to exchange={} with routing={}}", queue, exchange, routing))
                .onFailure().invoke(ex -> log.error("Failure unbinding queue={} to exchange={} with routing={}}", queue, exchange, routing, ex));
    }

    private Uni<Void> logOnlySubscriptionHandling(RabbitMQMessage rabbitMQMessage) {
        return Uni.createFrom().item(rabbitMQMessage)
                .onItem().transformToUni(msg -> {
                    var body = msg.body().toString();
                    log.info("[Log Only] Received body={}", body);
                    return Uni.createFrom().voidItem();
                });
    }

    public Uni<Void> addSubscription(UUID subscriberId, UUID friendId, ISubscriptionHandler subscriptionHandler) {
        String routingId = friendId.toString();
        String queue = "subscription.%s.%s".formatted(routingId, subscriberId);
        return declareQueue(queue)
                .chain(v -> declareBinding(queue, routingId))
                .invoke(v -> subscribe(queue, subscriberId, subscriptionHandler));
    }

    public Uni<Void> removeSubscription(UUID subscriberId, UUID friendId) {
        String routingId = friendId.toString();
        String queue = "subscription.%s.%s".formatted(routingId, subscriberId);
        return unbind(queue, routingId);
    }


    public void subscribe(String queue) {
        log.info("using default log only subscription handler={}", queue);
        subscribe(queue, null, (target, rabbitMQMessage) -> logOnlySubscriptionHandling(rabbitMQMessage));
    }

    public void subscribe(String queue, UUID userId, ISubscriptionHandler subscriptionHandler) {
        log.info("subscribe to queue={}", queue);
        rabbitMQClient.basicConsumer(queue)
                .onItem().transformToUni(rabbitMQConsumer -> rabbitMQConsumer
                        .exceptionHandler(th -> log.error("Exception handler error", th))
                        .toMulti()
                        .onItem().transformToUni(msg -> subscriptionHandler.handle(userId, msg))
                        .concatenate().collect().last()
                ).onItem().invoke(l -> log.info("THE END"))
                .replaceWithVoid()
                .subscribe().with(v -> {
                });
    }

    public Uni<Void> publish(String routingKey, Post wallPost) {//routingKey=userId of publisher
        log.info("Publishing to routing key={} post={}", routingKey, wallPost);
        return rabbitMQClient.basicPublish(NEW_POST_EXCHANGE, routingKey, serialize(wallPost))
                .onItem().invoke(ex -> log.info("Successfully published post with routing key={} and body={}", routingKey, wallPost))
                .onFailure().invoke(ex -> log.error("Failed to publish post with routing key={}", routingKey));
    }

    @SneakyThrows
    private Buffer serialize(Post post) {
        return Buffer.buffer(objectMapper.writeValueAsBytes(post));
    }

}
