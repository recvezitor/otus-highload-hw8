package com.dimas.core.controller;

import com.dimas.core.domain.mapper.PersonMapper;
import com.dimas.core.domain.mapper.PostMapper;
import com.dimas.core.service.FriendService;
import com.dimas.core.service.PostService;
import com.dimas.core.api.SecuredReactiveApi;
import com.dimas.core.api.model.ApiDialogMessage;
import com.dimas.core.api.model.ApiDialogUserIdSendPostRequest;
import com.dimas.core.api.model.ApiLoginPost200Response;
import com.dimas.core.api.model.ApiLoginPostRequest;
import com.dimas.core.api.model.ApiPost;
import com.dimas.core.api.model.ApiPostCreatePostRequest;
import com.dimas.core.api.model.ApiPostUpdatePutRequest;
import com.dimas.core.api.model.ApiUser;
import com.dimas.core.api.model.ApiUserRegisterPost200Response;
import com.dimas.core.api.model.ApiUserRegisterPostRequest;
import com.dimas.core.service.DialogService;
import com.dimas.core.service.PersonService;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.dimas.core.util.Const.DEFAULT_LIMIT;
import static com.dimas.core.util.Const.DEFAULT_OFFSET;
import static com.dimas.core.util.Const.MAX_LIMIT;
import static java.util.Objects.isNull;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class OpenApiController implements SecuredReactiveApi {

    private final DialogService dialogService;
    private final FriendService friendService;
    private final PersonService personService;
    private final PostService postService;
    private final PersonMapper personMapper;
    private final PostMapper postMapper;

    @Override
    @WithSpan
    public Uni<List<ApiDialogMessage>> dialogUserIdListGet(String userId) {
        return dialogService.getById(UUID.fromString(userId));
    }

    @Override
    public Uni<Response> dialogUserIdSendPost(String userId, ApiDialogUserIdSendPostRequest apiDialogUserIdSendPostRequest) {
        return dialogService.sendDialogMessage(UUID.fromString(userId), apiDialogUserIdSendPostRequest.getText())
                .map(v -> Response.ok().build());
    }

    @Override
    public Uni<ApiLoginPost200Response> loginPost(ApiLoginPostRequest apiLoginPostRequest) {
        return personService.login(UUID.fromString(apiLoginPostRequest.getId()), apiLoginPostRequest.getPassword())
                .map(response -> new ApiLoginPost200Response().token(response));
    }

    @Override
    public Uni<ApiUser> userGetIdGet(String id) {
        return personService.findById(UUID.fromString(id))
                .map(personMapper::map);
    }

    @Override
    public Uni<Integer> userFromCity(String city) {
        return personService.findByCity(city);
    }

    @Override
    public Uni<ApiUserRegisterPost200Response> userRegisterPost(ApiUserRegisterPostRequest apiUserRegisterPostRequest) {
        var request = personMapper.map(apiUserRegisterPostRequest);
        return personService.create(request)
                .map(person -> new ApiUserRegisterPost200Response().userId(person.getId().toString()));
    }

    @Override
    public Uni<List<ApiUser>> userSearchGet(String firstName, String lastName) {
        return personService.search(firstName, lastName);
    }

    @Override
    public Uni<String> postCreatePost(ApiPostCreatePostRequest apiPostCreatePostRequest) {
        var request = postMapper.map(apiPostCreatePostRequest);
        return postService.create(request)
                .map(post -> post.getId().toString());
    }

    @Override
    public Uni<Response> postDeleteIdPut(String id) {
        return postService.delete(UUID.fromString(id))
                .map(v -> Response.ok().build());
    }

    @Override
    public Uni<List<ApiPost>> postFeedGet(BigDecimal offset, BigDecimal limit) {
        return postService.feed(
                isNull(offset) ? DEFAULT_OFFSET : offset.longValue(),
                (isNull(limit) || limit.longValue() > MAX_LIMIT) ? DEFAULT_LIMIT : limit.longValue()
        );
    }

    @Override
    public Uni<ApiPost> postGetIdGet(String id) {
        return postService.getById(UUID.fromString(id))
                .map(postMapper::map);
    }

    @Override
    public Uni<Response> postUpdatePut(ApiPostUpdatePutRequest apiPostUpdatePutRequest) {
        var request = postMapper.map(apiPostUpdatePutRequest);
        return postService.update(request)
                .map(v -> Response.ok().build());
    }

    @Override
    public Uni<Response> friendDeleteUserIdPut(String userId) {
        return friendService.remove(UUID.fromString(userId))
                .map(v -> Response.ok().build());
    }

    @Override
    public Uni<Response> friendSetUserIdPut(String userId) {
        return friendService.add(UUID.fromString(userId))
                .map(v -> Response.ok().build());
    }

}
