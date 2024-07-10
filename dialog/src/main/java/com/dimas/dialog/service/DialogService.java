package com.dimas.dialog.service;

import com.dimas.dialog.domain.entity.DialogMessage;
import com.dimas.dialog.persistence.DialogRepository;
import com.dimas.dialog.util.HashUtil;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.list.KeyValue;
import io.quarkus.redis.datasource.list.Position;
import io.quarkus.redis.datasource.list.ReactiveListCommands;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@ApplicationScoped
public class DialogService {

    private final static int BATCH_SIZE = 500;
    private final DialogRepository dialogRepository;
    private final ReactiveListCommands<String, DialogMessage> commandList;


    public DialogService(DialogRepository dialogRepository, ReactiveRedisDataSource reactive) {
        this.dialogRepository = dialogRepository;
        commandList = reactive.list(String.class, DialogMessage.class);
    }


    public Uni<List<DialogMessage>> getDialog(UUID fromId, UUID toId) {
        log.debug("fromId={}, toId={}", fromId, toId);
        var dialogId = HashUtil.getDialogHash(fromId, toId);
        return dialogRepository.dialogList(dialogId);

    }

    public Uni<UUID> saveBlazingFastDialog(UUID fromId, UUID toId, String message) {
        log.debug("BLAZINGFAST fromId={}, toId={}, message={}", fromId, toId, message);
        var dialogId = HashUtil.getDialogHash(fromId, toId);
        var messageId = UUID.randomUUID();
        var dialog = DialogMessage.builder()
                .id(messageId)
                .dialogId(dialogId)
                .fromUser(fromId)
                .toUser(toId)
                .text(message)
                .build();
        return commandList.llen(dialogId)//should be transactional
                .onItem().transformToUni(len -> {
                    if (len < BATCH_SIZE) {
                        log.debug("saving to Redis");
                        return Uni.createFrom().item(dialog)
                                .flatMap(dlg -> commandList.rpush(dialogId, dialog))
                                .replaceWith(messageId);
                    } else {
                        log.debug("batch completed saving to PG");
                        return Uni.createFrom().item(dialog)
                                .flatMap(dlg -> commandList.blmpop(Duration.ofSeconds(1), Position.LEFT, BATCH_SIZE, dialogId))
                                .map(list -> list.stream().map(KeyValue::value).collect(toList()))
                                .map(list -> Stream.concat(Stream.of(dialog), list.stream()).collect(toList()))
//                                .invoke(list -> log.debug("list to save={}", list))
                                .flatMap(dialogRepository::addBatch)
                                .replaceWith(messageId);
                    }
                })
                .onFailure().invoke(th -> log.error("Failed to save", th));
    }

    public Uni<UUID> saveDialog(UUID fromId, UUID toId, String message) {
        log.debug("fromId={}, toId={}, message={}", fromId, toId, message);
        return dialogRepository.create(DialogMessage.builder()
                        .dialogId(HashUtil.getDialogHash(fromId, toId))
                        .fromUser(fromId)
                        .toUser(toId)
                        .text(message)
                        .build())
                .invoke(msg -> log.debug("DialogMessage successfully saved={}", msg))
                .map(DialogMessage::getId);
    }

}
