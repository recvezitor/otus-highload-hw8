package com.dimas.core.controller.exception;

import com.dimas.core.exception.MyJdbcException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Provider
@RequiredArgsConstructor
public class JdbcExceptionHandler implements ExceptionMapper<MyJdbcException> {

    @Override
    public Response toResponse(MyJdbcException exception) {
        return Response.status(400).entity(exception.getMessage()).build();
    }

}