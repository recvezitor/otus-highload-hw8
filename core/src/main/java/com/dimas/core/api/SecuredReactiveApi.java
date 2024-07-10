package com.dimas.core.api;

import com.dimas.core.api.model.ApiDialogUserIdSendPostRequest;
import com.dimas.core.api.model.ApiLoginPost200Response;
import com.dimas.core.api.model.ApiLoginPostRequest;
import com.dimas.core.api.model.ApiPost;
import com.dimas.core.api.model.ApiPostCreatePostRequest;
import com.dimas.core.api.model.ApiPostUpdatePutRequest;
import com.dimas.core.api.model.ApiUser;
import com.dimas.core.api.model.ApiUserRegisterPostRequest;
import com.dimas.core.controller.filter.Secured;
import com.dimas.core.api.model.ApiDialogMessage;
import com.dimas.core.api.model.ApiUserRegisterPost200Response;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;


@Path("")
@ApplicationScoped
public interface SecuredReactiveApi {

    /**
     * @param userId
     */
    @GET
    @Path("/dialog/{user_id}/list")
    @Produces({"application/json"})
    @Secured
    Uni<List<ApiDialogMessage>> dialogUserIdListGet(@PathParam("user_id") String userId);

    /**
     * @param userId
     * @param apiDialogUserIdSendPostRequest
     */
    @POST
    @Path("/dialog/{user_id}/send")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Secured
    Uni<Response> dialogUserIdSendPost(
            @PathParam("user_id") String userId,
            ApiDialogUserIdSendPostRequest apiDialogUserIdSendPostRequest
    );

    /**
     * Упрощенный процесс аутентификации путем передачи идентификатор пользователя и получения токена для дальнейшего прохождения авторизации
     *
     * @param apiLoginPostRequest
     */
    @POST
    @Path("/login")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    Uni<ApiLoginPost200Response> loginPost(ApiLoginPostRequest apiLoginPostRequest);

    /**
     * Получение анкеты пользователя
     *
     * @param id Идентификатор пользователя
     */
    @GET
    @Path("/user/get/{id}")
    @Produces({"application/json"})
    @Secured
    Uni<ApiUser> userGetIdGet(@PathParam("id") String id);


    /**
     * Получение количества пользователей в одном городе. Для проверки индексов на равенство
     *
     * @param city название города
     */
    @GET
    @Path("/user/city/{city}")
    @Produces({"application/json"})
    @Secured
    Uni<Integer> userFromCity(@PathParam("city") String city);

    /**
     * Регистрация нового пользователя
     *
     * @param apiUserRegisterPostRequest
     */
    @POST
    @Path("/user/register")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    Uni<ApiUserRegisterPost200Response> userRegisterPost(ApiUserRegisterPostRequest apiUserRegisterPostRequest);

    /**
     * Поиск анкет
     *
     * @param firstName Условие поиска по имени
     * @param lastName  Условие поиска по фамилии
     */
    @GET
    @Path("/user/search")
    @Produces({"application/json"})
    @Secured
    Uni<List<ApiUser>> userSearchGet(
            @QueryParam("first_name") String firstName,
            @QueryParam("last_name") String lastName
    );

//===========================================POST====================================//

    /**
     * @param apiPostCreatePostRequest
     */
    @POST
    @Path("/post/create")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Secured
    Uni<String> postCreatePost(ApiPostCreatePostRequest apiPostCreatePostRequest);

    /**
     * @param id
     */
    @PUT
    @Path("/post/delete/{id}")
    @Produces({"application/json"})
    @Secured
    Uni<Response> postDeleteIdPut(@PathParam("id") String id);

    /**
     * @param offset
     * @param limit
     */
    @GET
    @Path("/post/feed")
    @Produces({"application/json"})
    @Secured
    Uni<List<ApiPost>> postFeedGet(
            @QueryParam("offset") BigDecimal offset,
            @QueryParam("limit") BigDecimal limit
    );

    /**
     * @param id
     */
    @GET
    @Path("/post/get/{id}")
    @Produces({"application/json"})
    @Secured
    Uni<ApiPost> postGetIdGet(@PathParam("id") String id);

    /**
     * @param apiPostUpdatePutRequest
     */
    @PUT
    @Path("/post/update")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Secured
    Uni<Response> postUpdatePut(ApiPostUpdatePutRequest apiPostUpdatePutRequest);

//===========================================FRIEND====================================//

    /**
     * @param userId
     */
    @PUT
    @Path("/friend/delete/{user_id}")
    @Produces({"application/json"})
    @Secured
    Uni<Response> friendDeleteUserIdPut(@PathParam("user_id") String userId);

    /**
     * @param userId
     */
    @PUT
    @Path("/friend/set/{user_id}")
    @Produces({"application/json"})
    @Secured
    Uni<Response> friendSetUserIdPut(@PathParam("user_id") String userId);

}
