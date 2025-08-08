package vvu.centrauthz.errors;

public class ConflictError extends AppError {
    public ConflictError(String code, String message) {
        super(code, message);
    }

    public ConflictError(String message) {
        super("CONFLICT", message);
    }

    public ConflictError() {
        super("CONFLICT");
    }
}
