package com.dimas.core.domain.mapper;

import com.dimas.core.api.model.ApiDialogMessage;
import com.dimas.core.client.api.ApiRestDialogMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA;


@Mapper(componentModel = JAKARTA, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DialogMessageMapper {

//    @Mapping(source = "fromUser", target = "from")
//    @Mapping(source = "toUser", target = "to")
//    ApiDialogMessage map(DialogMessage source);

//    Post map(PostCreate source);
//
//    PostCreate map(ApiPostCreatePostRequest source);
//
//    PostUpdate map(ApiPostUpdatePutRequest source);

//    @Mapping(source = "fromUser", target = "from")
//    @Mapping(source = "toUser", target = "to")
//    @Mapping(source = "createdAt", target = "publishedAt")
    ApiDialogMessage map(ApiRestDialogMessage source);


}
