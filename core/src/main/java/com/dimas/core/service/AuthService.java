package com.dimas.core.service;

import com.dimas.core.domain.entity.AuthUser;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class AuthService {

    private final Map<UUID, String> storage = new ConcurrentHashMap<>();//move to redis for multiinstance case

    public boolean isValid(String token) {
        var pair = parseToken(token);
        return !isNull(pair) && token.equals(storage.get(pair.getItem1()));
    }

    public AuthUser fromToken(String token) {
        var pair = parseToken(token);
        if (isNull(pair)) return null;
        return AuthUser.builder().id(pair.getItem1()).build();
    }

    private Tuple2<UUID, String> parseToken(String token) {
        if (isNull(token)) return null;
        var parts = token.split("\\.");
        if (parts.length != 2) return null;
        return Tuple2.of(UUID.fromString(parts[0]), parts[1]);
    }

    public Uni<Void> save(UUID id, String token) {
        requireNonNull(id, "id cannot be null");
        requireNonNull(token, "token cannot be null");
        storage.put(id, token);
        return Uni.createFrom().voidItem();
    }

}
