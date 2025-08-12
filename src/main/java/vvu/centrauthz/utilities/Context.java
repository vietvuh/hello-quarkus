package vvu.centrauthz.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;
import java.util.function.Function;
import lombok.Builder;
import vvu.centrauthz.models.User;

/**
 * Execution context containing user information and attributes.
 *
 * @param user the current user information
 * @param token the authentication token
 * @param attributes additional context attributes as JSON
 */
@Builder(toBuilder = true)
public record Context(User user, String token, JsonNode attributes) {

    /**
     * Creates an empty context with all fields null.
     *
     * @return empty context instance
     */
    public static Context empty() {
        return Context.builder().build();
    }

    /**
     * Creates a context with the specified user.
     *
     * @param user the user for the context
     * @return context with user set
     */
    public static Context of(User user) {
        return Context.builder().user(user).build();
    }

    /**
     * Creates a context with a user containing the specified user ID.
     *
     * @param userId the user ID for the context
     * @return context with user ID set
     */
    public static Context of(UUID userId) {
        User user = User.builder().userId(userId).build();
        return Context.builder().user(user).build();
    }

    /**
     * Executes a function with this context and returns the result.
     *
     * @param <T> the return type of the function
     * @param function the function to execute with this context
     * @return the result of the function execution
     */
    public <T> T execute(Function<Context, T> function) {
        return function.apply(this);
    }
}
