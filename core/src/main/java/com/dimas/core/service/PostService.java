package com.dimas.core.service;

import com.dimas.core.api.model.ApiPost;
import com.dimas.core.domain.PostCreate;
import com.dimas.core.domain.PostUpdate;
import com.dimas.core.domain.entity.Person;
import com.dimas.core.domain.entity.Post;
import com.dimas.core.domain.mapper.PostMapper;
import com.dimas.core.persistence.PersonRepository;
import com.dimas.core.persistence.PostRepository;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class PostService {

    private final AuthenticationContext context;
    private final PostRepository postRepository;
    private final PersonRepository personRepository;
    private final PostMapper postMapper;
    private final RabbitService rabbitService;

    @Inject
    @CacheName("feed-cache")
    Cache cache;

    public Uni<Post> create(PostCreate request) {
        log.debug("currentUser={}", context.getCurrentUser());
        var userId = context.getCurrentUser().getId();
        return personRepository.findFriends(userId)
                .flatMap(this::invalidateList)
                .flatMap(v -> {
                    var post = postMapper.map(request).withFromUser(userId);
                    return postRepository.create(post);
                })
                .call(post -> rabbitService.publish(userId.toString(), post));
    }

    private Uni<Void> invalidateList(List<Person> ids) {
        log.debug("Invalidating {} friends feeds", ids.size());
        return Multi.createFrom().iterable(ids)
                .onItem().transformToUni(person -> cache.invalidate(person.getId().toString()))
                .concatenate().collect().asList()
                .replaceWithVoid();
    }

    public Uni<Void> update(PostUpdate request) {
        return postRepository.update(request);
    }

    public Uni<Post> getById(UUID id) {
        return postRepository.getById(id);
    }

    public Uni<Void> delete(UUID id) {
        return postRepository.delete(id)
                .replaceWithVoid();
    }

    public Uni<List<ApiPost>> feed(Long offset, Long limit) {
        log.debug("currentUser={}", context.getCurrentUser());
        var userId = context.getCurrentUser().getId();
        log.debug("GET FEED");
        return cache.getAsync(userId.toString(), f -> {
                    log.debug("CACHE MISS");
                    return postRepository.feed(userId, offset, limit)
                            .map(list -> list.stream().map(postMapper::map).toList());
                })
                .invoke(l -> log.debug("FEED END"));

    }

}
