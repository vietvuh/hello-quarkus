package vvu.centrauthz.errors;

/**
 * Error Utilities
 */
public class EUtils {
    EUtils() {
        throw new IllegalStateException();
    }

    public static NotFoundError createNotFoundError(String message) {
        return new NotFoundError("NOT_FOUND", message);
    }

    public static ConflictError createConflictError(String message) {
        return new ConflictError("CONFLICT", message);
    }

    public static BadRequestError createBadRequestError(String message) {
        return new BadRequestError(message);
    }

    public static NotImplementedError createNotImplementedError(String message) {
        return new NotImplementedError(message);
    }

    public static NotImplementedError createNotImplementedError() {
        return new NotImplementedError("Feature not implemented");
    }

}
