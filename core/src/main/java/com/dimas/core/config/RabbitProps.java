package com.dimas.core.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "rabbitmq")
public interface RabbitProps {

    String virtualHost();

    String username();

    String password();

    String hostname();

    Integer port();

    String toString();

}
