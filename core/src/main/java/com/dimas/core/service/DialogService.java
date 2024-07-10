package com.dimas.core.service;

import com.dimas.core.api.model.ApiDialogMessage;
import com.dimas.core.client.DialogRestClient;
import com.dimas.core.client.api.ApiRestDialogNewMessage;
import com.dimas.core.domain.mapper.DialogMessageMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
//@RequiredArgsConstructor
public class DialogService {

    //По идее тут надо вебкоет публикацию делать? или возможно это потом при декомпозиции на микросервисы добавить а сейчас типа акцент на шардировании


    public DialogService(@RestClient  DialogRestClient dialogRestClient, DialogMessageMapper dialogMessageMapper, AuthenticationContext context) {
        this.dialogRestClient = dialogRestClient;
        this.dialogMessageMapper = dialogMessageMapper;
        this.context = context;
    }

    private final DialogRestClient dialogRestClient;
    private final DialogMessageMapper dialogMessageMapper;
    private final AuthenticationContext context;

    public Uni<List<ApiDialogMessage>> getById(UUID toUser) {
        log.debug("currentUser={}", context.getCurrentUser());
        var userId = context.getCurrentUser().getId();
        return dialogRestClient.get(userId, toUser)
                .map(list -> list.stream().map(dialogMessageMapper::map).toList());
    }

    public Uni<Void> sendDialogMessage(UUID toUser, String text) {
        log.debug("currentUser={}", context.getCurrentUser());
        var userId = context.getCurrentUser().getId();
        return dialogRestClient.save(userId, toUser,
                        ApiRestDialogNewMessage.builder()
                                .text(text)
                                .build())
                .invoke(msg -> log.debug("DialogMessage successfully saved={}", msg))
                .replaceWithVoid();
    }

}
