package com.dimas.dialog.controller.rest;

import com.dimas.dialog.controller.rest.api.model.ApiDialogMessage;
import com.dimas.dialog.domain.entity.DialogMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA;


@Mapper(componentModel = JAKARTA, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DialogMessageMapper {

    @Mapping(source = "fromUser", target = "from")
    @Mapping(source = "toUser", target = "to")
    @Mapping(source = "createdAt", target = "publishedAt")
    ApiDialogMessage map(DialogMessage source);

}
