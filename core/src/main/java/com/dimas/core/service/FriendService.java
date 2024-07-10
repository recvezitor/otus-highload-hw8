package com.dimas.core.service;

import com.dimas.core.domain.FriendRequestCreate;
import com.dimas.core.persistence.FriendRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    private final AuthenticationContext context;
    private final RabbitService rabbitService;
    private final WebsocketService websocketService;

    public Uni<Void> add(UUID friendId) {
        var user = context.getCurrentUser();
        log.debug("currentUser={}", user);
        return friendRepository.create(FriendRequestCreate.builder().userId(user.getId()).friendId(friendId).build())
                .flatMap(v-> rabbitService.addSubscription(user.getId(), friendId, websocketService));
    }

    public Uni<Void> remove(UUID friendId) {
        var user = context.getCurrentUser();
        log.debug("currentUser={}", user);
        return friendRepository.remove(user.getId(), friendId)
                .flatMap(v-> rabbitService.removeSubscription(user.getId(), friendId));
    }

}
