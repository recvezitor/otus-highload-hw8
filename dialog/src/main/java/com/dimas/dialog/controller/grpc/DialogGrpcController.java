package com.dimas.dialog.controller.grpc;

import com.dimas.dialog.sdk.grpc.ApiDialogMessageRequest;
import com.dimas.dialog.sdk.grpc.ApiDialogMessageResponse;
import com.dimas.dialog.sdk.grpc.ApiDialogNewMessageRequest;
import com.dimas.dialog.sdk.grpc.ApiDialogNewMessageResponse;
import com.dimas.dialog.sdk.grpc.ApiDialogService;
import com.dimas.dialog.service.DialogService;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class DialogGrpcController implements ApiDialogService {

    private final DialogService dialogService;
    private final DialogGrpcMapper dialogGrpcMapper;

    @Override
    public Uni<ApiDialogMessageResponse> get(ApiDialogMessageRequest request) {
        return dialogService.getDialog(UUID.fromString(request.getFrom()), UUID.fromString(request.getTo()))
                .map(dialogGrpcMapper::toResponse);
    }

    @Override
    public Uni<ApiDialogNewMessageResponse> save(ApiDialogNewMessageRequest request) {
        return dialogService.saveDialog(UUID.fromString(request.getFrom()), UUID.fromString(request.getTo()), request.getText())
                .map(resp -> ApiDialogNewMessageResponse.newBuilder()
                        .setId(resp.toString())
                        .build());
    }

}