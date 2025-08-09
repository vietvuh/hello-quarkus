package vvu.centrauthz.models;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

@Builder(toBuilder = true)
public record Patcher<T> (@NotNull List<String> fields, @NotNull T data) {
    public Patcher {
        if (Objects.isNull(fields)) {
            throw new IllegalArgumentException("fields is required");
        }

        if (Objects.isNull(data)) {
            throw new IllegalArgumentException("data is required");
        }
    }
}
