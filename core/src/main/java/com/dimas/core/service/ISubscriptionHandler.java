package com.dimas.core.service;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.rabbitmq.RabbitMQMessage;

import java.util.UUID;


public interface ISubscriptionHandler {

    Uni<Void> handle(UUID target, RabbitMQMessage rabbitMQMessage);

}
