package com.dimas.core.domain.mapper;

import com.dimas.core.api.model.ApiPost;
import com.dimas.core.api.model.ApiPostCreatePostRequest;
import com.dimas.core.api.model.ApiPostUpdatePutRequest;
import com.dimas.core.domain.PostCreate;
import com.dimas.core.domain.PostUpdate;
import com.dimas.core.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA;


@Mapper(componentModel = JAKARTA, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {

    ApiPost map(Post source);

    Post map(PostCreate source);

    PostCreate map(ApiPostCreatePostRequest source);

    PostUpdate map(ApiPostUpdatePutRequest source);


}
