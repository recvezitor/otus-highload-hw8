package com.dimas.dialog.controller.rest;

import com.dimas.dialog.controller.rest.api.ApiDialogRest;
import com.dimas.dialog.controller.rest.api.model.ApiDialogMessage;
import com.dimas.dialog.controller.rest.api.model.ApiDialogNewMessage;
import com.dimas.dialog.service.DialogService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class DialogRestController implements ApiDialogRest {

    private final DialogService dialogService;

    private final DialogMessageMapper dialogMessageMapper;

    @Override
    public Uni<List<ApiDialogMessage>> get(UUID fromId, UUID toId) {
        return dialogService.getDialog(fromId, toId)
                .map(list -> list.stream().map(dialogMessageMapper::map).toList());
    }

    @Override
    public Uni<UUID> save(UUID fromId, UUID toId, ApiDialogNewMessage message) {
        return dialogService.saveDialog(fromId, toId, message.getText());
    }

    @Override
    public Uni<UUID> saveFast(UUID fromId, UUID toId, ApiDialogNewMessage message) {
        return dialogService.saveBlazingFastDialog(fromId, toId, message.getText());
    }

}
