package com.dimas.core.persistence;

import com.dimas.core.domain.FriendRequestCreate;
import com.dimas.core.domain.entity.FriendRequest;
import com.dimas.core.util.Const;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class FriendRepository {

    private final PgPool pgPool;

    public Uni<Void> create(FriendRequestCreate request) {
        log.info("persisting FriendRequestCreate={}", request);
        return pgPool.withTransaction(conn -> conn.preparedQuery("""
                        INSERT INTO %s.friend_request (user_id, friend_id, created_at)
                                    VALUES ($1, $2, $3)
                        """.formatted(Const.SCHEMA_NAME))
                .execute(map(request))
                .replaceWithVoid()
        );
    }

    public Uni<Void> remove(UUID userID, UUID friendID) {
        log.info("removing friend={}", friendID);
        return pgPool.withTransaction(conn -> conn.preparedQuery("""
                        DELETE FROM %s.friend_request
                        WHERE user_id=$1 AND friend_id=$2
                        """.formatted(Const.SCHEMA_NAME))
                .execute(Tuple.tuple()
                        .addUUID(userID)
                        .addUUID(friendID))
                .replaceWithVoid()
        );
    }

    private FriendRequest map(Row row) {
        return FriendRequest.builder()
                .userId(row.getUUID("user_id"))
                .friendId(row.getUUID("friend_id"))
                .build();
    }

    private Tuple map(FriendRequestCreate request) {
        return Tuple.tuple()
                .addUUID(request.getUserId())
                .addUUID(request.getFriendId())
                .addLocalDateTime(LocalDateTime.now());
    }

}
