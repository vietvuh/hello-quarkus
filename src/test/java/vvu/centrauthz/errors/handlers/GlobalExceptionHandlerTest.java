package vvu.centrauthz.errors.handlers;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import vvu.centrauthz.errors.AppError;
import vvu.centrauthz.errors.ErrorUtils;
import vvu.centrauthz.errors.IllegalJsonValue;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalErrorHandler = new GlobalExceptionHandler();

    @Test
    void toStatus_BadRequestError() {
        AppError error = ErrorUtils.createBadRequestError(UUID.randomUUID().toString());
        assertEquals(Response.Status.BAD_REQUEST, GlobalExceptionHandler.toStatus(error));

        error = ErrorUtils.createConflictError(UUID.randomUUID().toString());
        assertEquals(Response.Status.CONFLICT, GlobalExceptionHandler.toStatus(error));

        error = ErrorUtils.createNotFoundError(UUID.randomUUID().toString());
        assertEquals(Response.Status.NOT_FOUND, GlobalExceptionHandler.toStatus(error));

        error = ErrorUtils.createNotImplementedError(UUID.randomUUID().toString());
        assertEquals(Response.Status.NOT_IMPLEMENTED, GlobalExceptionHandler.toStatus(error));

        error = new IllegalJsonValue("");
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR, GlobalExceptionHandler.toStatus(error));
    }

    @Test
    void testHandleAppError() {
        AppError error = ErrorUtils.createBadRequestError(UUID.randomUUID().toString());
        try (var response = globalErrorHandler.handleAppError(error)) {
            assertEquals(response.getStatus(), GlobalExceptionHandler.toStatus(error).getStatusCode());
            assertSame(response.getEntity(), error.getError());
        }
    }

    @Test
    void testHandleGenericException() {
        AppError appError = ErrorUtils.createBadRequestError(UUID.randomUUID().toString());
        var error = new IllegalArgumentException(appError);
        try (var response = globalErrorHandler.handleGenericException(error)) {
            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        }

    }
}