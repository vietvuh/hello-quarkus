package vvu.centrauthz.errors.handlers;

import io.quarkus.arc.ArcUndeclaredThrowableException;
import io.quarkus.runtime.util.ExceptionUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import vvu.centrauthz.errors.*;
import vvu.centrauthz.models.Error;

/**
 * Global lException Handler.
 */
@Slf4j
public class GlobalExceptionHandler {

    static Response.Status toStatus(AppError ex) {
        return switch (ex) {
            case BadRequestError ignored -> Response.Status.BAD_REQUEST;
            case ConflictError ignored -> Response.Status.CONFLICT;
            case NotFoundError ignored -> Response.Status.NOT_FOUND;
            case NotImplementedError ignored -> Response.Status.NOT_IMPLEMENTED;
            default -> Response.Status.INTERNAL_SERVER_ERROR;
        };
    }

    static Response.Status toStatus(ClientErrorException ex) {
        return Response.Status.fromStatusCode(ex.getResponse().getStatus());
    }

    static String getMessage(ClientErrorException ex) {
        if (Objects.nonNull(ex.getCause())) {
            return ex.getCause().getMessage();
        } else {
            return ex.getMessage();
        }
    }

    static boolean isValidationError(ClientErrorException ex) {
        return ex.getCause() instanceof IllegalArgumentException;
    }

    static Error toError(ClientErrorException ex) {
        var errorBuilder = Error.builder();

        if (isValidationError(ex)) {
            errorBuilder.code("VALIDATION_ERROR");
        } else {
            errorBuilder.code(Response.Status.fromStatusCode(ex.getResponse().getStatus()).name());
        }

        errorBuilder.message(getMessage(ex));

        return errorBuilder.build();
    }

    /**
     * Handle Constraint Violation Exception.
     */
    @ServerExceptionMapper
    public RestResponse<Error> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("Validation constraint violation: {}", ex.getMessage());

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        Map<String, String> details = violations.stream()
                .collect(Collectors.toMap(
                    violation -> violation.getPropertyPath().toString(),
                    ConstraintViolation::getMessage,
                    (existing, replacement) -> existing + "; " + replacement
                ));

        Error error = Error.builder()
                .code("VALIDATION_ERROR")
                .details(details)
                .build();

        return RestResponse.status(Response.Status.BAD_REQUEST, error);
    }

    /**
     * Handle AppError.
     */
    @ServerExceptionMapper
    public RestResponse<Error> handleAppError(AppError ex) {
        return RestResponse
            .status(toStatus(ex), ex.getError());
    }

    /**
     * Handle ClientErrorException.
     */
    @ServerExceptionMapper
    public RestResponse<Error> handleClientErrorException(ClientErrorException ex) {
        return RestResponse
                .status(toStatus(ex), toError(ex));
    }

    /**
     * Handle Generic Exception.
     */
    @ServerExceptionMapper
    public RestResponse<Error> handleGenericException(Exception ex) {
        log.info(ExceptionUtil.generateStackTrace(ex));
        var error = Error
                .builder()
                .code(Response.Status.INTERNAL_SERVER_ERROR.name())
                .message(ex.getMessage())
                .build();
        return RestResponse
            .status(Response.Status.INTERNAL_SERVER_ERROR, error);
    }

    private RestResponse<Error> processPersistenceException(RuntimeException e) {
        Throwable ex = e.getCause();
        while (Objects.nonNull(ex)) {
            if (ex instanceof org.hibernate.exception.ConstraintViolationException) {
                log.error(ex.getMessage());
                var error = Error
                        .builder()
                        .code(Response.Status.CONFLICT.name())
                        .message(ex.getMessage())
                        .build();
                return RestResponse
                    .status(Response.Status.CONFLICT, error);
            } else {
                ex = ex.getCause();
            }
        }
        return handleGenericException(e);
    }

    /**
     * Handle ArcUndeclaredThrowableException.
     */
    @ServerExceptionMapper
    public RestResponse<Error> handleArcException(ArcUndeclaredThrowableException ex) {
        return processPersistenceException(ex);
    }
}
