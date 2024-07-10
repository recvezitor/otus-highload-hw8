package com.dimas.dialog.controller.rest.api;

import com.dimas.dialog.controller.rest.api.model.ApiDialogMessage;
import com.dimas.dialog.controller.rest.api.model.ApiDialogNewMessage;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import java.util.List;
import java.util.UUID;


@Path("/dialog")
public interface ApiDialogRest {

    @GET
    @Path("/{from_id}/{to_id}")
    @Produces({"application/json"})
    Uni<List<ApiDialogMessage>> get(
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
            @Valid ApiDialogNewMessage message
    );

    @POST
    @Path("/fast/{from_id}/{to_id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    Uni<UUID> saveFast(
            @NotNull @PathParam("from_id") UUID fromId,
            @NotNull @PathParam("to_id") UUID toId,
            @Valid ApiDialogNewMessage message
    );

}
