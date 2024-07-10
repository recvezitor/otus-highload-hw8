package com.dimas.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.quarkus.jackson.ObjectMapperCustomizer;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import static com.dimas.core.util.ObjectMapperFactory.createObjectMapper;

@Singleton
@Slf4j
public class RegisterObjectMapperCustomizer implements ObjectMapperCustomizer {

//    @Produces
////    RabbitMQClient rabbitMQClient(Vertx vertx, RabbitMQOptions config) {
//    RabbitMQClient rabbitMQClient(Vertx vertx, RabbitProps rabbitProps) {
////        RabbitMQOptions config = new RabbitMQOptions();
////        config.setUser("admin");
////        config.setPassword("admin");
////        config.setHost("localhost");
////        config.setPort(5672);
////         config.setVirtualHost("/");
////         config.setVirtualHost("vhost1");
////        log.info("Autowired RabbitMQOptions={}", config);
////        return RabbitMQClient.create(vertx, config);
//        return RabbitMQClient.create(vertx);
//    }

    @Produces
    RabbitMQClient rabbitMQClient(Vertx vertx, RabbitProps rabbitProps) {
        log.info("Autowired RabbitProps={}", rabbitProps);
        RabbitMQOptions config = new RabbitMQOptions();
        config.setUser(rabbitProps.username());
        config.setPassword(rabbitProps.password());
        config.setHost(rabbitProps.hostname());
        config.setPort(rabbitProps.port());
        config.setVirtualHost(rabbitProps.virtualHost());
        return RabbitMQClient.create(vertx, config);
    }

//    @Produces
//    @Identifier("my-named-options")
//    public RabbitMQOptions getNamedOptions() {
////        PemKeyCertOptions keycert = new PemKeyCertOptions()
////                .addCertPath("./tls/tls.crt")
////                .addKeyPath("./tls/tls.key");
////        PemTrustOptions trust = new PemTrustOptions().addCertPath("./tlc/ca.crt");
//        // You can use the produced options to configure the TLS connection
//        return new RabbitMQOptions()
////                .setSsl(true)
////                .setPemKeyCertOptions(keycert)
////                .setPemTrustOptions(trust)
//                .setUser("user1")
//                .setPassword("password1")
//                .setHost("localhost")
//                .setPort(5672)
//                .setVirtualHost("vhost1")
//                .setConnectionTimeout(6000) // in milliseconds
//                .setRequestedHeartbeat(60) // in seconds
//                .setHandshakeTimeout(6000) // in milliseconds
//                .setRequestedChannelMax(5)
//                .setNetworkRecoveryInterval(500) // in milliseconds
//                .setAutomaticRecoveryEnabled(true);
//    }


    public void customize(ObjectMapper mapper) {
        createObjectMapper(mapper).
                setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

}