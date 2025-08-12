package vvu.centrauthz.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Supplier;
import vvu.centrauthz.errors.IllegalJsonValue;

/**
 * Utility class for JSON operations and ObjectMapper access.
 */
public class JsonTools {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Private constructor to prevent instantiation.
     */
    JsonTools() {
        throw new IllegalStateException();
    }

    /**
     * Executes a supplier within a JSON context, wrapping exceptions.
     *
     * @param <T> the return type of the supplier
     * @param supplier the operation to execute
     * @return the result of the supplier
     * @throws IllegalJsonValue if any exception occurs during execution
     */
    static <T> T jsonContext(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new IllegalJsonValue(e);
        }
    }

    /**
     * Returns the shared ObjectMapper instance.
     *
     * @return the configured ObjectMapper
     */
    public static ObjectMapper mapper() {
        return MAPPER;
    }
}
