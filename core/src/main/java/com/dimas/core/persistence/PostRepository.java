package com.dimas.core.persistence;

import com.dimas.core.domain.PostUpdate;
import com.dimas.core.domain.entity.Post;
import com.dimas.core.exception.NotFoundJdbcException;
import com.dimas.core.util.Const;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class PostRepository {

    private final PgPool pgPool;

    public Uni<Post> create(Post request) {
        log.debug("persisting request post={}", request);
        return pgPool.withTransaction(conn -> conn.preparedQuery("""
                        INSERT INTO %s.post (from_user, text, created_at)
                                    VALUES ($1, $2, $3)
                                    RETURNING id
                        """.formatted(Const.SCHEMA_NAME))
                .execute(map(request))
                .flatMap(rowSet -> {
                    var entity = request.withId(rowSet.iterator().next().getUUID("id"));
                    return Uni.createFrom().item(entity);
                })
        );
    }

    public Uni<Integer> delete(UUID id) {
        log.debug("deleting request postId={}", id);
        return pgPool.withTransaction(conn -> conn.preparedQuery("""
                        DELETE FROM %s.post WHERE id = $1
                        """.formatted(Const.SCHEMA_NAME))
                .execute(Tuple.tuple().addUUID(id))
                .onItem().transformToUni(rowSet -> Uni.createFrom().item(rowSet.size())));
    }

    public Uni<Void> update(PostUpdate request) {
        log.info("updating request post={}", request);
        return pgPool.withTransaction(conn -> conn.preparedQuery("""
                        UPDATE %s.post SET text = $1 WHERE id = $2
                        """.formatted(Const.SCHEMA_NAME))
                .execute(Tuple.tuple().addString(request.getText()).addUUID(request.getId()))
                .replaceWithVoid());
    }


    public Uni<Post> getById(UUID id) {
        return findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundJdbcException("Post not found for id='%s'".formatted(id)));
    }

    public Uni<Post> findById(UUID id) {
        final var query = """
                 select * from %s.post where id = $1 LIMIT 1
                """.formatted(Const.SCHEMA_NAME);
        return pgPool.preparedQuery(query)
                .execute(Tuple.tuple().addUUID(id))
                .onItem().transformToUni(rowSet -> {
                    for (Row row : rowSet) {
                        return Uni.createFrom().item(map(row));//return first item
                    }
                    return Uni.createFrom().nullItem();
                });
    }

    public Uni<List<Post>> feed(UUID userId, Long offset, Long limit) {
        log.debug("repository feed call");
        final var query = """
                SELECT * FROM %s.post as p
                INNER JOIN %s.friend_request fr
                ON p.from_user = fr.user_id
                WHERE fr.friend_id = $1
                ORDER BY p.created_at DESC
                OFFSET $2 LIMIT $3
                """.formatted(Const.SCHEMA_NAME, Const.SCHEMA_NAME);
        return pgPool.preparedQuery(query)
                .execute(Tuple.tuple()
                        .addUUID(userId)
                        .addLong(offset)
                        .addLong(limit)
                )
                .onItem().transformToUni(rowSet -> {
                    List<Post> result = new ArrayList<>();
                    for (Row row : rowSet) {
                        result.add(map(row));
                    }
                    return Uni.createFrom().item(result);
                });
    }


    private Post map(Row row) {
        return Post.builder()
                .id(row.getUUID("id"))
                .fromUser(row.getUUID("from_user"))
//                .toUser(row.getUUID("to_user"))
                .text(row.getString("text"))
                .createdAt(row.getLocalDateTime("created_at"))
                .updatedAt(row.getLocalDateTime("updated_at"))
                .build();
    }

    private Tuple map(Post request) {
        return Tuple.tuple()
                .addUUID(request.getFromUser())
//                .addUUID(request.getToUser())
                .addString(request.getText())
                .addLocalDateTime(isNull(request.getCreatedAt()) ? LocalDateTime.now() : request.getCreatedAt());
    }

}
