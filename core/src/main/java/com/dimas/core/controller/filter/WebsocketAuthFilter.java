package com.dimas.core.controller.filter;

import com.dimas.core.service.AuthService;
import com.dimas.core.service.AuthenticationContextImpl;
import com.dimas.core.util.Const;
import io.quarkus.vertx.web.RouteFilter;
import io.smallrye.common.vertx.VertxContext;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.HttpException;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;


@Slf4j
@Provider
@RequiredArgsConstructor
@Priority(Priorities.AUTHENTICATION + 1)
public class WebsocketAuthFilter {
    public static final String UPGRADE_HEADER = "Upgrade";

    @Inject
    AuthService authService;

    @Inject
    AuthenticationContextImpl authCtx;

    @RouteFilter(Priorities.AUTHENTICATION + 10)
    void authFilter(RoutingContext rc) {
        Uni.createFrom().item(() -> {
                    final HttpServerRequest currentRequest = rc.request();
                    if (currentRequest.headers().contains(UPGRADE_HEADER) && "websocket".equals(currentRequest.getHeader(UPGRADE_HEADER))) {
                        String authorizationHeader = currentRequest.headers().get(HttpHeaders.AUTHORIZATION);
                        if (isNull(authorizationHeader)) {
                            return Uni.createFrom().failure(new HttpException(401, "auth header is null"));
                        }
                        String token = authorizationHeader.substring(AuthenticationSecurityFilter.AUTHENTICATION_SCHEME.length()).trim();
                        if (!authService.isValid(token)) return Uni.createFrom().failure(new HttpException(403, "Invalid token"));
                        var user = authService.fromToken(token);
                        authCtx.setCurrentUser(user);
                        log.debug("user={}", user);
                        VertxContext.getOrCreateDuplicatedContext().putLocal(Const.VERTEX_CONTEXT_SESSION_ID, user.getId());
                    }
                    return Uni.createFrom().voidItem();
                })
                .invoke(rc::next)
                .onFailure().invoke(rc::fail)
                .subscribe().with(v -> {
                });
    }

}
