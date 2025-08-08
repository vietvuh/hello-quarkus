package vvu.centrauthz.errors;

import lombok.Getter;
import vvu.centrauthz.models.Error;

/**
 * Application-specific error exception that carries an Error response.
 */
@Getter
public class AppError extends RuntimeException {
    private final Error error;

    public AppError(Error error) {
        super(error.message());
        this.error = error;
    }

    public AppError(String code) {
        super(code);
        this.error = Error.builder()
            .code(code)
            .build();
    }

    public AppError(String code, Throwable e) {
        super(e);
        this.error = Error.builder()
            .code(code)
            .message(e.getMessage())
            .build();
    }

    public AppError(String code, String message) {
        super(message);
        this.error = Error.builder()
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public String toString() {
        return "{ error=" + error + '}';
    }
}
