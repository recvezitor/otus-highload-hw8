package com.dimas.dialog.controller.grpc;

import com.dimas.dialog.domain.entity.DialogMessage;
import com.dimas.dialog.sdk.grpc.ApiDialogMessage;
import com.dimas.dialog.sdk.grpc.ApiDialogMessageResponse;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;


@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DialogGrpcMapper {

    @Mapping(source = "fromUser", target = "from")
    @Mapping(source = "toUser", target = "to")
    @Mapping(source = "createdAt", target = "publishedAt")
    ApiDialogMessage map(DialogMessage source);

    default ApiDialogMessageResponse toResponse(List<DialogMessage> source) {
        var list = source.stream().map(this::map).collect(Collectors.toList());
        return ApiDialogMessageResponse.newBuilder()
                .addAllDialog(list)
                .build();
    }

    default Timestamp toTimestamp(LocalDateTime value) {
        if (isNull(value)) return Timestamp.getDefaultInstance();
        return Timestamp.newBuilder()
                .setSeconds(value.toInstant(ZoneOffset.UTC).getEpochSecond())
                .setNanos(value.toInstant(ZoneOffset.UTC).getNano())
                .build();
    }

}
