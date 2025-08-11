package vvu.centrauthz.models;

import java.util.List;
import java.util.Objects;
import lombok.Builder;

/**
 * Paginated result container with data and next page token.
 *
 * @param <T> the type of data items in the page
 * @param <N> the type of the next page token
 * @param data the list of items in this page
 * @param next the token for retrieving the next page
 */
@Builder(toBuilder = true)
public record Page<T,N>(List<T> data, N next) {

    /**
     * Compact constructor that validates required fields.
     */
    public Page {
        if (Objects.isNull(data)) {
            throw new IllegalArgumentException("data is required");
        }
    }
}
