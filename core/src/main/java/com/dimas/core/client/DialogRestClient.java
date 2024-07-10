package com.dimas.core.client;

import com.dimas.core.client.api.ApiRestDialogMessage;
import com.dimas.core.client.api.ApiRestDialogNewMessage;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.UUID;

@RegisterRestClient(configKey = "dialog-api")
public interface DialogRestClient {

    @GET
    @Path("/{from_id}/{to_id}")
    @Produces({"application/json"})
    Uni<List<ApiRestDialogMessage>> get(
            @NotNull @PathParam("from_id") UUID fromId,
            @NotNull @PathParam("to_id") UUID toId
    );

    @POST
    @Path("/{from_id}/{to_id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    Uni<UUID> save(
            @NotNull @PathParam("from_id") UUID fromId,
            @NotNull @PathParam("to_id") UUID toId,
            ApiRestDialogNewMessage message
    );

    @POST
    @Path("/fast/{from_id}/{to_id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    Uni<UUID> saveFast(
            @NotNull @PathParam("from_id") UUID fromId,
            @NotNull @PathParam("to_id") UUID toId,
            ApiRestDialogNewMessage message
    );

}