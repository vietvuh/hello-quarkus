package vvu.centrauthz.models;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Builder;

/**
 * Container for patch operations with field tracking.
 *
 * @param <T> the type of data being patched
 * @param fields list of fields to be updated
 * @param data the patch data containing new values
 */
@Builder(toBuilder = true)
public record Patcher<T>(@NotNull List<String> fields, @NotNull T data) {

    /**
     * Compact constructor that validates required fields.
     */
    public Patcher {
        if (Objects.isNull(fields)) {
            throw new IllegalArgumentException("fields is required");
        }

        if (Objects.isNull(data)) {
            throw new IllegalArgumentException("data is required");
        }
    }

    /**
     * Executes operation if the specified field is present in the patch.
     *
     * @param field the field name to check
     * @param execute the operation to perform if field is present
     * @return this patcher for method chaining
     */
    public Patcher<T> having(String field, Consumer<T> execute) {
        if (fields.contains(field)) {
            execute.accept(this.data);
        }
        return this;
    }

}
